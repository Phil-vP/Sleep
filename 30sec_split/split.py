# -*- coding: utf-8 -*-
"""
Created on Fri Jan 29 18:30:45 2021

@author: Philipp
"""

import datetime
import glob


def readFile(pathname):
    
    with open(pathname) as fp:
        allLines = fp.read().splitlines()
    
    
    for l in allLines:
        print(l)
    
    
    totalDuration = int(allLines[0])
    startTime = allLines[1]
    endTime = allLines[2]
    timestampLines = allLines[4:]
    
    offset = 0
    
    allTimes = []
    
    for l in timestampLines:
        split = l.split(',')
        totalSeconds = int(split[2]) - offset
        appendCount = int(totalSeconds/30)
        rest = totalSeconds % 30
        
        if offset > 15:
            appendCount += 1
        
        if rest >= 15:
            appendCount += 1
        
        extendList = split[3] * appendCount
        print("Extended the list by " + split[3] + ", " + str(appendCount) + " times")
        print("Offset? " + str(offset))
        print("Rest? " + str(rest) + "\n")
        
        offset = (30-rest)
        allTimes.extend(extendList)
        
    
    
    print(allTimes)
    
    print(len(allTimes))
    
    date = datetime.datetime.strptime(startTime, '%H:%M')
    
    fout = open(''.join(pathname.split(".")[:-1]) + ".csv", "w+")
    
    for i in range(len(allTimes)):
        string = str(date.time()) + " , " + allTimes[i] + "\n"
        date += datetime.timedelta(seconds=30)
        
        fout.write(string)
    
    
    
    fout.close()


def readAllFiles():
    allFiles = glob.glob("B:\\Projekte\\Schlaf\\Auswertung\\*txt")
    
    for file in allFiles:
        readFile(file)
    

if __name__== "__main__":
    readAllFiles()