import csv
import os
from os import path
import sys
import argparse
import matplotlib.pyplot as plt 
import statistics

'''
Simple script that plots the number of events created during the simulation.
Should be used with a transitive interest model, since it counts the type of events created
and in the open model we only have 1 type of event!
'''

def parse_arg(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('--file', type=str, help='Path to the log file')
    parser.add_argument('--group', type=int, help='Ticks grouping')
    return parser.parse_args(argv)

def addEvent(event, list):
    if("Follow" in event):
        list[1]+=1
    elif("Unfollow" in event):
        list[2]+=1
    elif("Block" in event):
        list[3]+=1
    elif("Unblock" in event):
        list[4]+=1
    else:
        list[0]+=1

def read_events(file_path):
    file_events = open(str(file_path))
    csv_events = csv.DictReader(file_events, delimiter=',')

    events = {}

    for row in csv_events:
        tick = row['tick']
        if tick not in events.keys():
            #events[tick]=[# stream events, # follows, # unfollows, ...]
            events[tick] = [0,0,0,0,0]
        
        if row['createdEvents'] != "":
            loggedEvents = row['createdEvents'].split(",")

            [addEvent(event, events[tick]) for event in loggedEvents]

    #return a list where the position is the tick and values is the array with event count
    return list(events.values())

#reduces the amount of ticks displayed on graph
#if the simulation has 500 ticks and sensibility is 10,
#the graph will have 50 elements on the x axis
def groupByTick(events, sensibility):
    groupedEvents = []

    iteration = 0
    newIteration = True

    for i in range(len(events)):
        if(newIteration):
            groupedEvents.append(events[i])
            newIteration = False
        else:
            for j in range(len(groupedEvents[iteration])):
                groupedEvents[iteration][j]+=events[i][j]

        if(i % sensibility == 0):
            newIteration = True
            iteration+=1


    return groupedEvents


def plot(events, sensibility):
    #x axis in the tick number
    x = [i*sensibility for i in range(len(events))]

    labels = ["Stream", "Follow", "Unfollow", "Block", "Unblock"]

    for i in range(5):
        y = [events[j][i] for j in range(len(events))]

        plt.plot(x, y, label = labels[i])

    plt.xlabel('Tick')
    plt.ylabel('Created events')

    plt.title('Created events')
    # show a legend on the plot
    plt.legend()
    # Display a figure.
    plt.show()

def main():
    args = parse_arg(sys.argv[1:])

    if(args.file == None):
        exit(1)
    
    events = read_events(args.file)
    plot(groupByTick(events, int(args.group)), int(args.group))

if __name__ == "__main__":
    main()
