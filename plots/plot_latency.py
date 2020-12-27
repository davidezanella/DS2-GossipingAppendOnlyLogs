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
    parser.add_argument('--file', type=str, help='Path to the log file', required=True)
    parser.add_argument('--type', type=str, help='Type of plot in [Basic, MeanPrefLANs, MeanWait]', required=True)
    return parser.parse_args(argv)


def get_plot_key_and_subkey(row, plot_type):
    if plot_type == 'Basic':
        key = (row['MeanPrefLANs'], row['StdPrefLANs'], row['MeanWait'], row['StdWait'])
        subkey = (row['LANs'], row['Persons'])
    elif plot_type == 'MeanPrefLANs':
        key = (row['LANs'], row['Persons'], row['StdPrefLANs'], row['MeanWait'], row['StdWait'])
        subkey = (row['MeanPrefLANs'],)
    elif plot_type == 'MeanWait':
        key = (row['LANs'], row['Persons'], row['MeanPrefLANs'], row['StdPrefLANs'], row['StdWait'])
        subkey = (row['MeanWait'],)
    
    return key, subkey


def get_plot_series_label(key, plot_type):
    if plot_type == 'Basic':
        return r'$\mathcal{{N}}_{{prefLANs}}({}, {}) - \mathcal{{N}}_{{wait}}({}, {})$'.format(*key)
    elif plot_type == 'MeanPrefLANs':
        return ''
    elif plot_type == 'MeanWait':
        return ''


def get_plot_xy_label(plot_type):
    if plot_type == 'Basic':
        return 'LANs number', 'Mean latency'
    elif plot_type == 'MeanPrefLANs':
        return r'$\mu_{prefLANs}$', 'Mean latency'
    elif plot_type == 'MeanWait':
        return r'$\mu_{wait}$', 'Mean latency'


def read_latencies(file, plot_type):
    file_latencies = open(str(file))
    csv_latencies = list(csv.DictReader(file_latencies, delimiter=','))

    latencies = {}

    for row in csv_latencies:
        key, subkey = get_plot_key_and_subkey(row, plot_type)

        if key not in latencies:
            latencies[key] = {}
        if subkey not in latencies[key]:
            latencies[key][subkey] = []

        latencies[key][subkey].append(float(row['Latency']))

    for label in latencies:
        for key in latencies[label]:
            latencies[label][key] = statistics.mean(latencies[label][key])

    return latencies


def plot(latencies, plot_type):
    fig = plt.figure()
    ax = fig.gca()

    for label in latencies:
        xs, ys, zs = [], [], []
        for key, value in latencies[label].items():
            xs.append(int(key[0]))
            ys.append(value)
            if len(key) > 1:
                zs.append(int(key[1]))
            else:
                zs.append(1)

        label_txt = get_plot_series_label(label, plot_type)
        ax.plot(xs, ys, label=label_txt, alpha=0.4)
        ax.scatter(xs, ys, s=zs, marker='o')

    x_lbl, y_lbl = get_plot_xy_label(plot_type)
    ax.set_xlabel(x_lbl)
    ax.set_ylabel(y_lbl)
    
    plt.title('Latency plot')
    plt.legend()
    plt.show()
    

def main():
    args = parse_arg(sys.argv[1:])

    latencies = read_latencies(args.file, args.type)
    plot(latencies, args.type)


if __name__ == "__main__":
    main()
