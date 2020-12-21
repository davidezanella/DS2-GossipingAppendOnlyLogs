import csv
import os
from os import path
import sys
import argparse
import matplotlib.pyplot as plt 
import statistics

'''
Script that plots the diffusion of the different events during the simulation, calculating the latency.
Two modes:
 - Open for the openModel, Transitive for the transitiveInterestModel

If --target option is specified, instead of plotting the graph the script will append the results to a csv file
'''

def parse_arg(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('--file', type=str, help='Path to the log file', required=True)
    parser.add_argument('--mode', type=str, help='Either "Open" or "Transitive"', required=True, choices=["Open", "Transitive"])
    parser.add_argument('--target', type=str, help='Csv to write to')
    return parser.parse_args(argv)

#read the file and create a dictionary 
def read_events_open(file):
    file_events = open(str(file))
    csv_events = csv.DictReader(file_events, delimiter=',')

    events = {}

    for row in csv_events:
        if row['createdEvents'] != "":
            loggedEvents = row['createdEvents'].split(",")

            for event in loggedEvents:
                events[event] = [int(row['tick']), []]

        if row['arrivedEvents'] != "":
            loggedEvents = row['arrivedEvents'].split(",")

            for event in loggedEvents:
                events[event][1].append(int(row['tick']))

    #return a dict event: <creation_tick, [arrival_tick1, arrival_tick2, ...]>
    return events

#calculate the latency in case of an open model
def latency_open(events):
    for event in events:
        events[event][1] = statistics.mean(events[event][1])
        events[event] = events[event][1] - events[event][0]

    #return a dict event: latency
    return events

def plot_open(events):
    plt.plot(events.keys(), events.values())
    plt.xlabel('Events')
    plt.ylabel('Latency')
    plt.title('Events diffusion latency')
    plt.show()

def main():
    args = parse_arg(sys.argv[1:])

    if(args.target == None):
        #just plot the graph for a single run
        if(args.mode=="Open"):
            plot_open(latency_open(read_events_open(events)))
        elif(args.mode=="Transitive"):
            print()
    else:
        #just append the results to a csv file
        print()


        

if __name__ == "__main__":
    main()
