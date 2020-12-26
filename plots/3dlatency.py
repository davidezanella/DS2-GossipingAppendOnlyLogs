import csv
import os
from os import path
import sys
import argparse
import matplotlib.pyplot as plt 
from mpl_toolkits.mplot3d import Axes3D
import statistics

'''
Script that plots the 3d graph of LAN/Persons/Latency.
'''

def parse_arg(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('--file', type=str, help='Path to the log file')
    return parser.parse_args(argv)

#returns a dict (LANs, Persons): Latency
def read_latencies(file):
    file_latencies = open(str(file))
    csv_latencies = list(csv.DictReader(file_latencies, delimiter=','))

    latencies = {}

    for row in csv_latencies:
        if (row['LANs'], row['Persons']) not in latencies:
            latencies[(row['LANs'], row['Persons'])] = [float(row['Latency'])]
        else:
            latencies[(row['LANs'], row['Persons'])].append(float(row['Latency']))

    for key in latencies:
        latencies[key] = statistics.mean(latencies[key])

    return latencies

def plot(latencies):
    fig = plt.figure()
    ax = fig.gca(projection='3d')

    xs, ys, zs = [], [], []
    for key, value in latencies.items():
        xs.append(int(key[0]))
        ys.append(int(key[1]))
        zs.append(value)

    ax.plot(xs, ys, zs)
    ax.set_xlabel('LANs number')
    ax.set_ylabel('People number')
    ax.set_zlabel('Mean latency')
    
    plt.show()
    

def main():
    args = parse_arg(sys.argv[1:])

    latencies = read_latencies(args.file)
    plot(latencies)

if __name__ == "__main__":
    main()
