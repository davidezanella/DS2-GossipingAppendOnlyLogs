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

If --target option is specified, instead of plotting the graph the script will append the results to a csv file and
is intended for use with a batch file. If target is NOT specified, the script will plot the graph of the latencies by
reading a non-batch log file.
'''

def parse_arg(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('--file', type=str, help='Path to the log file', required=True)
    parser.add_argument('--mode', type=str, help='Either "Open" or "Transitive"', required=True, choices=["Open", "Transitive"])
    parser.add_argument('--strategy', type=str, help='Either "Random" or "Habit"', choices=["Random", "Habit"])
    parser.add_argument('--target', type=str, help='Csv to write to')
    return parser.parse_args(argv)

#read the file and create a dictionary 
def read_events_open(file):
    file_events = open(str(file))
    csv_events = list(csv.DictReader(file_events, delimiter=','))

    events = {}

    for row in csv_events:
        if row['createdEvents'] != "":
            created_events = row['createdEvents'].split(",")

            for event in created_events:
                events[event] = [float(row['tick']), []]

    for row in csv_events:
        if row['arrivedEvents'] != "":
            arrived_events = row['arrivedEvents'].split(",")

            for event in arrived_events:
                events[event][1].append(float(row['tick']))

    #return a dict events: <creation_tick, [arrival_tick1, arrival_tick2, ...]>
    return events

def read_batch_open(file):
    file_events = open(str(file))
    csv_events = list(csv.DictReader(file_events, delimiter=','))

    events = {}

    for row in csv_events:
        run = row['run']

        if(run not in events.keys()):
            events[run]={}

        if row['createdEvents'] != "":
            created_events = row['createdEvents'].split(",")

        for event in created_events:
            events[run][event] = [float(row['tick']), []]     

    for row in csv_events:
        run = row['run']

        if row['arrivedEvents'] != "":
            arrived_events = row['arrivedEvents'].split(",")

            for event in arrived_events:
                events[run][event][1].append(float(row['tick']))

    #return a dict run: { event: <creation_tick, [arrival_tick1, arrival_tick2, ...]>}
    return events



#calculate the latency in case of an open model
def latency_open(events):
    for event in events:
        if len(events[event][1]) > 0:
            mean_arrived_tick = statistics.mean(events[event][1])
            events[event] = mean_arrived_tick - events[event][0]
        else:
            events[event] = -1

    #return a dict event: latency
    return events

def latency_batch_open(events, strategy, mode):
    rows = []

    for run in events:
        run_latencies = []

        for event in events[run]:
            if len(events[run][event][1]) > 0:
                mean_arrived_tick = statistics.mean(events[run][event][1])
                run_latencies.append(mean_arrived_tick - events[run][event][0])
        
        rows.append({
            'Strategy': str(strategy),
            'Mode': str(mode),
            'Latency': str(statistics.mean(run_latencies))
        })

    return rows

    
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
        plot_open(latency_open(read_events_open(args.file)))
    else:
        #calculate mean of batch run and append to file
        rows = latency_batch_open(read_batch_open(args.file), args.strategy, args.mode)
        #remove the print when editing this part
        file_exists = path.exists(args.target)
        with open(args.target, 'a') as fd:
            writer = csv.DictWriter(fd, fieldnames=rows[0].keys())

            if not file_exists:
                writer.writeheader()

            writer.writerows(rows)


        

if __name__ == "__main__":
    main()
