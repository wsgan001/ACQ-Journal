from math import *
from random import *
import sys
import math
import numpy as np
# import sobol_seq
from timeit import default_timer as timer


def NormDist_Stand(x):
	return (1.0 + erf(x / math.sqrt(2.0))) / 2.0


def NormDist_Diff(x):
	return 1.0 * math.exp(-0.5 * x * x) / math.sqrt(2 * math.pi)


def OptPrice(cp, StockPrice, StrikePrice, t, T, vol, rate):
	if (cp != 1 and cp != -1):
		raise Exception("Invaild cp value, should be 1 for call or -1 for put", cp)
	d1 = (math.log(1.0 * StockPrice / StrikePrice) + rate * (T - t)) / vol / math.sqrt(T - t) + 0.5 * vol * math.sqrt(
		T - t)
	d2 = d1 -  vol * math.sqrt(T - t)
	return cp * StockPrice * NormDist_Stand(cp * d1) - cp * StrikePrice * math.exp(-rate * (T - t)) * NormDist_Stand(
		cp * d2)



def OptPricewithq(cp, StockPrice, StrikePrice, tau, vol, rate, q):
	if (cp != 1 and cp != -1):
		raise Exception("Invaild cp value, should be 1 for call or -1 for put", cp)
	d1 = (math.log(1.0 * StockPrice / StrikePrice) + (rate - q) * tau) / vol / math.sqrt(tau) + 0.5 * vol * math.sqrt(
		tau)
	d2 = d1 - vol * math.sqrt(tau)
	price = cp * StockPrice * math.exp(-q * tau) * NormDist_Stand(cp * d1) - cp * StrikePrice * math.exp(
		-rate * tau) * NormDist_Stand(cp * d2)
	return (price, d1)


def impliedVolwithq(OptPrice, cp, StockPrice, StrikePrice, tau, rate, q):
	n = 0
	Nmax = 100
	sigmadiff = 1.0
	sigmahat = math.sqrt(2 * math.fabs((math.log(1.0 * StockPrice / StrikePrice) + (rate - q) * tau) / tau))
	sigma = sigmahat
	targetValue = OptPrice  # scallOptPricewithq(S,K,tau,0.2,r,q)[0]
	while (sigmadiff > 1e-8 and n < Nmax):
		[p, d1] = OptPricewithq(cp, StockPrice, StrikePrice, tau, sigma, rate, q)
		sigmadiff = (p - targetValue) / (StockPrice * math.exp(-q * tau) * math.sqrt(tau) * NormDist_Diff(d1))
		sigma = sigma - sigmadiff
		sigmadiff = math.fabs(sigmadiff)
		n = n + 1
	return sigma



def expiryDate(x):
	return dt.datetime(int(x) // 10000, int(x) % 10000 // 100, int(x) % 100, 15, 0, 0)

def standNormal(row,col):
	np.random.seed(5)
	random = np.zeros((row,col))
	for i in range (row):
		random[i, :] = np.random.standard_normal(col)
	return random

# def standNormal(row,col):
# 	return sobol_seq.i4_sobol_generate_std_normal(row, col)

def geoAsianOption(StockPrice, vol, rate, T, K, N, cp=1):
	sigma = vol * np.sqrt(1.0 * (N+1) * (2*N+1) / (6*N**2))
	mu = (rate-0.5*vol**2) * (N+1)/(2*N) + 0.5*sigma**2
	return OptPricewithq(cp, StockPrice, K, T, sigma, rate, rate - mu)[0]

def geoBasketOption(StockPrice, vol, rate, T, K ,corr, cp,n=2):
	n = n*1.0
	sigma = 1/n * np.sqrt(vol[0]**2 + vol[0]*vol[1]*corr* 2 + vol[1]**2)
	mu = rate - 0.5*np.sum(vol**2)/n + 0.5*sigma**2
	basketPrice = np.power(StockPrice.cumprod()[-1],1/n)
	return OptPricewithq(cp, basketPrice, K, T, sigma, rate, rate - mu)[0]

def conVariMethod(Varith,Vgeo,Vgeo_true):
	print('using Control variates :')
	covXY = np.mean(Varith*Vgeo) - (np.mean(Vgeo) * np.mean(Varith))
	theta = covXY / np.var(Vgeo)
	z = Varith + theta*(Vgeo_true - Vgeo)
	return np.mean(z)

def arithAsianOption(StockPrice, vol, rate, T, K, step, path, conVar,cp=1):
	dt = 1.0 * T / step
	c1 = rate - 0.5 * vol ** 2
	c2 = vol * np.sqrt(dt)
	#t = np.cumprod( np.exp(c1 * dt + c2 * standNormal(path,step)) , axis=1 )
	paths = StockPrice * np.exp(c1 * dt + c2 * standNormal(path,step)).cumprod(axis=1)

	arithMean = paths.mean(1)
	geoMean = np.exp(1.0 / step * np.log(paths).sum(1))

	arith_payoff = np.exp(-rate*T) * np.maximum(cp*(arithMean - K), 0)
	geo_payoff   = np.exp(-rate*T) * np.maximum(cp*(geoMean - K), 0)

	#Standard Monte Carlo
	if conVar == 'NULL':
		return np.mean(arith_payoff)

	#Control variates by default
	geo = geoAsianOption(StockPrice, vol,rate, T, K, step, cp)
	return conVariMethod(arith_payoff,geo_payoff,geo)


def arithBasketOption(StockPrice,vol, rate, T, K, corr, cp, path, conVar):

	dt = 1.0 * T #/ step
	c1 = rate - 0.5 * vol ** 2
	c2 = vol * np.sqrt(dt)

	z = standNormal(path, 2)
	z[:,1] = corr*z[:,0] + np.sqrt(1-corr**2)*z[:,1]

	paths = StockPrice * np.exp(c1 * dt + c2 * z)

	arithMean = 0.5 * np.sum(paths,1)
	geoMean = np.exp( 0.5 * (np.log(paths[:,0]) + np.log(paths[:,1])) )

	arith_payoff = np.exp(-rate*T) * np.maximum(cp*(arithMean - K), 0)
	geo_payoff   = np.exp(-rate*T) * np.maximum(cp*(geoMean - K), 0)

	#Standard Monte Carlo
	if conVar == 'NULL':
		return np.mean(arith_payoff)

	#Monte Carlo with control variates
	geo = geoBasketOption(StockPrice, vol,rate, T, K, corr,cp)
	return conVariMethod(arith_payoff,geo_payoff,geo)

def OptionBinTree(StockPrice, vol, rate, T, K, N, cp,American='True'):
	K = 1.0 * K
	N = int(N)
	dt = 1.0*T/N
	u = np.exp(vol*np.sqrt(dt))
	d = 1.0/u
	p = (np.exp(rate*dt) - d) / (u-d)
	DF = np.exp(-rate*dt)

	stock = np.asarray([StockPrice * u**(N - j) * d**j for j in range(N + 1)])
	#payoff at leaves
	payoff = np.maximum(cp*(stock-K),0)
	#start rolling back
	for i in range(N-1, -1, -1):
		#vectorize computation
		payoff = DF * (p * payoff[:-1] + (1-p) * payoff[1:])
		stock = stock[:-1]*d
		if(American):
			#Simply check if the option is worth more alive or dead
			payoff = np.maximum(payoff,cp*(stock-K))
	#payoff will be a integer eventually
	return payoff[0]

if __name__ == '__main__':
	s = 100
	T = 3
	r = 0.05
	m = 100000
	ts = timer()
	print(arithAsianOption(s,0.3,r,T,100,50,m,conVar=0,cp=-1))
	print('run time:',timer()-ts)
	# print(arithAsianOption(s,0.3,r,T,100,50,m,1))
	# print(arithAsianOption(s,0.3,r,T,100,50,m,conVar='NULL',cp=-1))
	# print(arithAsianOption(s,0.3,r,T,100,50,m,1,-1))
	# print(geoAsianOption(s,0.3,r,T,100,50))

	S=np.asarray([100,100])
	sigma = np.asarray([0.3,0.3])
	cor = 0.5
	# print(geoBasketOption(S,sigma,r,T,100,cor,1))
	# ts = timer()
	# print(arithBasketOption(S,sigma,r,T,100,cor,1,m,conVar='NULL'))
	# print(arithBasketOption(S,sigma,r,T,100,cor,1,m,1))
	# te = timer()
	# print(te-ts)
	# print(arithBasketOption(S,sigma,r,T,100,cor,-1,m,conVar='NULL'))
	# print(arithBasketOption(S,sigma,r,T,100,cor,-1,m,1))
