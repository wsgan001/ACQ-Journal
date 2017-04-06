import math
import numpy as np
from scipy.stats import norm
from timeit import default_timer as timer #for timing

def d1BS(S, K, T, t, sigma, q, r):
    return (    math.log(S / K) + (r - q) * (T - t)) / (sigma * math.sqrt(T - t)) \
        + 0.5*sigma * math.sqrt(T - t) 
        
def d2BS(S, K, T, t, sigma, q, r):
    return (math.log(S /K) + (r - q) * (T - t)) / (sigma * math.sqrt(T - t))\
         -0.5* sigma * math.sqrt(T - t)

#black-scholes func for call price
def CBS(S, K, T, t, sigma, q, r):
    return S * math.exp((-q) * (T - t)) * norm.cdf(d1BS(S, K, T, t, sigma, q, r))\
         - K * math.exp((-r) * (T - t)) * norm.cdf(d2BS(S, K, T, t, sigma, q, r))

def PBS(S, K, T, t, sigma, q, r):
    return K * math.exp(-r * (T - t)) * norm.cdf(-d2BS(S, K, T, t, sigma, q, r))\
         - S * math.exp(-q * (T - t)) * norm.cdf(-d1BS(S, K, T, t, sigma, q, r))
         
#Initiate guess of sigma in newton method
def initial_guess(S, K, t, T, q, r):
    return math.sqrt(2 * abs( (math.log(S / K) + (r - q) * (T - t)) / (T - t)))