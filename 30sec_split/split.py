# -*- coding: utf-8 -*-
"""
Created on Fri Jan 29 18:30:45 2021

@author: Philipp
"""


def readFile(pathname):
    
    with open(pathname) as fp:
        allLines = fp.read().splitlines()
    
    
    for l in allLines:
        print(l)
    # fout = open("output.txt", "w+")
    # fout.write("Moinsen")
    # fout.close()


def readAllFiles():
    readFile("B:\\Projekte\\Schlaf\\Auswertung\\VP01_N1.txt")

if __name__== "__main__":
    readAllFiles()