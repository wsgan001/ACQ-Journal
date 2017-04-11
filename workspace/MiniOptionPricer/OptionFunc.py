import math
import numpy as np
from scipy.stats import norm
from timeit import default_timer as timer #for timing

def dBS(S, K, T, t, sigma, q, r, type):
    if type=='d1':
        return (    math.log(S / K, math.e) + (r - q) * (T - t)) / (sigma * math.sqrt(T - t)) \
        + 0.5*sigma * math.sqrt(T - t) 
    elif type=='d2':
        return (math.log(S /K, math.e) + (r - q) * (T - t)) / (sigma * math.sqrt(T - t))\
         -0.5* sigma * math.sqrt(T - t)
    else:
        print "error"   

#black-scholes func for call/put option
def BS(S, K, T, t, sigma, q, r,type):
    if type=='C':
        return S * math.exp((-q) * (T - t)) * norm.cdf(dBS(S, K, T, t, sigma, q, r,'d1'))\
         - K * math.exp((-r) * (T - t)) * norm.cdf(dBS(S, K, T, t, sigma, q, r,'d2'))
    elif type=='P':
        return K * math.exp(-r * (T - t)) * norm.cdf(-dBS(S, K, T, t, sigma, q, r,'d2'))\
         - S * math.exp(-q * (T - t)) * norm.cdf(-dBS(S, K, T, t, sigma, q, r,'d1'))
    else:
        print "error"
    
         
#Initiate guess of sigmahat with newton method
def initGuess(S, K, T, t, q, r):
    return math.sqrt(2 * abs( (math.log(S / K, math.e) + (r - q) * (T - t)) / (T - t)))

#Implied volatility for call option
def IVC(S, K, T, t, q, r, cTrue):
    lowBound = 1e-8
    sigma = initGuess(S, K, T, t, q, r)
    sigmaDiff = 1.0
    n = 1
    nMax = 1000
    while sigmaDiff >= lowBound and n < nMax :
        #f(xn)
        c = BS(S, K, T, t, sigma, q, r, 'C')
        fn = c - cTrue
        #f'(xn)
        fn1 = S * math.exp((-q) * (T - t)) * math.sqrt(T - t) * norm._cdf(dBS(S,K,T,t,sigma,q,r,'d1'))
        increment = fn / fn1 * 0.1
        sigma = sigma - increment
        n += 1
        sigmaDiff = abs(increment)
    return sigma

def IVP(S, K, T, t, q, r, pTrue):
    lowBound = 1e-8
    sigmaHat = initGuess(S, K, T, t, q, r)
    sigmaA, sigmaB = sigmaHat, sigmaHat
    sigmaDiff = 1.0
    n = 1
    nMax = 1000
    while sigmaDiff >= lowBound and n < nMax:
        if n==1:
            p = BS(S, K, t, T, sigmaHat, q, r,'P')
        else:
            p = BS(S, K, t, T, sigmaB, q, r,'P')
        fn = p-pTrue
        fn1 = S*math.exp((0-q)*(T-t))*math.sqrt(T-t)*norm._cdf(dBS(S,K,t,T,sigmaB, q, r,'d1'))
        if fn1 == 0:
            return 0.0
        increment = fn/fn1 * 0.1
        if fn>0:
            sigmaA = sigmaB
            sigmaB = sigmaB-increment
        else:
            sigmaMid = (sigmaA+sigmaB)/2
            while sigmaA-sigmaB > lowBound:
                sigmaMid = (sigmaA+sigmaB)/2
                if BS(S, K, T, t, sigmaMid, q, r,'P')-pTrue == 0:
                    return sigmaMid
                elif (BS(S, K, t, T, sigmaMid, q, r)-pTrue)*(BS(S, K, t, T, sigmaB, q, r)-pTrue)<0:
                    sigmaA = sigmaMid
                else:
                    sigmaB = sigmaMid
            return abs(sigmaMid)
        n += 1
        sigmaDiff = abs(increment)
    return 

#risk free
def d(S, K, T, sigma, mu,type):
    if type=='d1':
        return (math.log(S/K, math.e)+(mu+0.5*pow(sigma, 2))*T)/(math.sqrt(T)*sigma)
    elif type=='d2':
        return (math.log(S/K, math.e)+(mu-0.5*pow(sigma, 2))*T)/(math.sqrt(T)*sigma)
    else:
        print "error"



#time-zore value of geometric Asian option
def geoAsianOption(S, sigma, r, t, K, n, type):
    N = float(n)
    T = float(t)
    sigmaHat = sigma*math.sqrt((N+1)*(2*N+1)/(6*pow(N,2)))
    muHat = (r-0.5*pow(sigma, 2))*(N+1)/(2*N)+0.5*pow(sigmaHat, 2)

    d1 = d(S, K, T, sigmaHat, muHat,'d1')
    d2 = d(S, K, T, sigmaHat, muHat,'d2')

    if type == 'C':
        return math.exp(-r * T) * (S * math.exp(muHat * T)*norm.cdf(d1) - K*norm.cdf(d2))
    elif type=='P':
        return math.exp(-r * T) * (K * norm.cdf(-d2) -S * math.exp(muHat * T) * norm.cdf(-d1))
    else:
        print "error"


#closed-form formula for C/P on Geometric basket
def geoBasket(S1, S2, sigma1, sigma2, r, T, K ,corr, type):
    sigma = math.sqrt(sigma1 ** 2 + sigma1 * sigma2 * corr * 2 + sigma2 ** 2) / 2
    mu = r-0.25*(pow(sigma1, 2)+pow(sigma2, 2))+0.5*pow(sigma, 2)
    B = math.sqrt(S1*S2)
    d1 = d(B, K, T, sigma, mu,'d1')
    d2 = d(B, K, T, sigma, mu,'d2')
    if type == 'C':
        return math.exp(-r*T)*(B*math.exp(mu*T)*norm.cdf(d1)-K*norm.cdf(d2))
    elif type == 'P':
        return math.exp(-r*T)*(-B*math.exp(mu*T)*norm.cdf(-d1)+K*norm.cdf(-d2))
    else:
        print "error"


#Arithmetic Asian option
#Input: S sigma r T K n type path cv
#path: number paths for Monte Carlo simulation
#cv: type of control variate (null or geo_asian)
# @vectorize(['f8(f8, f8, f8, f8, f8)'], target='gpu')
# def browinanMotion(S, dt, c1, c2, random):
#     return S * math.exp(c1 * dt + c2 * random)
def browinanMotion(S, random, r, sigma,T, step,):
    dt = float(T) / step
    return S * np.exp((r - 0.5 * sigma * sigma) * dt + sigma * np.sqrt(dt)* random)


def arithAsianOption(S, sigma, r, T, K, step, type, path, cvtype):
    np.random.seed(0)
    paths = np.zeros((path, step), order='F')
    random = np.zeros((path, step), order='F')
    for i in range(0, path):
        random[i, :] = np.random.standard_normal(step)
    

    #initialize first step result
    paths[:, 0] = browinanMotion(S, sigma, T, random[:, 0])

    #simulate the remaining steps in monte carlo
    for i in range(1, step):
        s = paths[:, i - 1]
        paths[:, i] = browinanMotion(s, sigma, T, random[:, i])

    arithMean = paths.mean(1)
    logPaths = np.log(paths)
    geoMean = np.exp(1 / float(step) * logPaths.sum(1))

    if type == 'C':
        arith_payoff = np.maximum(arithMean - K, 0)*np.exp(-r*T)
        geo_payoff = np.maximum(geoMean - K, 0)*np.exp(-r*T)
    elif type == 'P':
        arith_payoff = np.maximum(K - arithMean, 0)*np.exp(-r*T)
        geo_payoff = np.maximum(K - geoMean, 0)*np.exp(-r*T)
    else:
        return 'error'

    #Standard Monte Carlo
    if cvtype == 'NULL':
        return np.mean(arith_payoff)

    #Control variates
    else:
        XY = arith_payoff*geo_payoff
        covXY = np.mean(XY) - (np.mean(geo_payoff) * np.mean(arith_payoff))
        theta = covXY/np.var(geo_payoff)
        geo = geoAsianOption(S, sigma,r, T, K, step, type)
        z = arith_payoff + theta*(geo - geo_payoff)
        return np.mean(z)


#arithmetric basket
#Input: S1 S2 sigma1 sigma r T K corr type path cv
#path: number paths for Monte Carlo simulation
#cv: type of control variate (null or geo_basket)
def arith_basket(S1, S2, sigma1, sigma2, r, T, K, corr, type, path, cv):
    np.random.seed(0)
    z1 = np.random.standard_normal(path)
    z = np.random.standard_normal(path)
    z2 = corr*z1+math.sqrt(1-corr**2)*z
    S1_T = S1*np.exp((r-0.5*sigma1**2)*T+sigma1*np.sqrt(T)*z1)
    S2_T = S2*np.exp((r-0.5*sigma2**2)*T+sigma2*np.sqrt(T)*z2)
    ba_T = (S1_T+S2_T)/2
    bg_T = np.exp((np.log(S1_T)+np.log(S2_T))/2)
    if type == 'C':
        arith_payoff = (ba_T-K)*math.exp(-r*T)
        geo_payoff = (bg_T-K)*math.exp(-r*T)
    else:
        arith_payoff = (K-ba_T)*math.exp(-r*T)
        geo_payoff = (K-bg_T)*math.exp(-r*T)
    for i in range(0, path):
        arith_payoff[i] = max(arith_payoff[i], 0)
        geo_payoff[i] = max(geo_payoff[i], 0)

    #Standard Monte Carlo
    if cv == 'NULL':
        p_mean = np.mean(arith_payoff)
        p_std = np.std(arith_payoff)
        return p_mean
    #Monte Carlo with control variates
    else:
        XY = [0.0]*path
        for i in range(0, path):
            XY[i] = arith_payoff[i]*geo_payoff[i]
        covXY = np.mean(XY) - np.mean(arith_payoff)*np.mean(geo_payoff)
        theta = covXY/np.var(geo_payoff)

        geo = geoBasket(S1, S2, sigma1, sigma2, r, T, K ,corr, type)
        Z = arith_payoff + theta * (geo - geo_payoff)
        z_mean = np.mean(Z)
        z_std = np.std(Z)
        return z_mean

def bino_tree(S, K, r, T, sigma, N, type):
    dt = float(T)/N
    u = math.exp(sigma*math.sqrt(dt))
    d = 1/u
    p = (math.exp(r*dt)-d)/(u-d)
    DF = math.exp(-r*dt)
    stock = [0.0]*(N+1)
    pay_off = [0.0]*(N+1)
    if type == 'C':
        for i in range(0, N+1):
            stock[i] = S*pow(u, N-i)*pow(d, i)
            pay_off[i] = max(S*pow(u, N-i)*pow(d, i)-K, 0)
        for j in range(0, N):
            for i in range(0, N-j):
                stock_price = math.sqrt(stock[i]*stock[i+1])
                pay_off[i] = max(stock_price-K, DF*(p*pay_off[i]+(1-p)*pay_off[i+1]))
                stock[i] = stock_price
        return pay_off[0]
    else:
        for i in range(0, N+1):
            stock[i] = S*pow(u, N-i)*pow(d, i)
            pay_off[i] = max(-S*pow(u, N-i)*pow(d, i)+K, 0)
        for j in range(0, N):
            for i in range(0, N-j):
                stock_price = math.sqrt(stock[i]*stock[i+1])
                pay_off[i] = max(-stock_price+K, DF*(p*pay_off[i]+(1-p)*pay_off[i+1]))
                stock[i] = stock_price
        return pay_off[0]

if __name__ == '__main__':
    ts = timer()
    arithAsianOption(100, 0.3, 0.05, 3.0, 100, 50, 'P', 1000000, 'CV')
    te = timer()
    elapsed = te - ts
    print elapsed

