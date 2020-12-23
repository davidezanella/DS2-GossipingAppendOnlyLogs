import csv
import os
from os import path
import sys
import argparse
import matplotlib.pyplot as plt 
import statistics

'''
Script that plots the diffusion of the different events during the simulation, calculating the persons reached.
Two modes:
 - Open for the openModel, Transitive for the transitiveInterestModel

If --target option is specified, instead of plotting the graph the script will append the results to a csv file and
is intended for use with a batch file. If target is NOT specified, the script will plot the graph of the latencies by
reading a non-batch log file.
'''

def parse_arg(argv):
    parser = argparse.ArgumentParser()
    #parser.add_argument('--file', type=str, help='Path to the log file')
    #parser.add_argument('--group', type=int, help='Ticks grouping')
    return parser.parse_args(argv)