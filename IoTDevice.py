"""
// Overall Goal
Create a python file which will:
    - creates dummy data to be put into the DB [10 rows] 
    - create a json/csv file to be given to the DB
    - sends that csv file to the DB 
"""
# Needed Imports
import random
import pandas as pd
from faker import Faker

# Global variables
generatedData = [] # where the dummy data will be stored 
# parking lots and their ids numbers
parkingLots = {
    "Bursary":3030,
    "Chancellor hall":2321,
    "Chemistry":4311,
    "Commuters":3411,
    "Dramatic theatre":9090,
    "Law":4547,
    "Main Library":4659,
    "Main Parking":8943,
    "Med sci":5435,
    "MSBM":3469,
    "Norman Manly Law and soci":5848,
    "Sci tech parking lot front":4636,
    "Sci tech parking rear front gate":9570,
    "Sickle Cell unit":6578,
    "Soci and msbm":9646,
    "Taylor hall ":2078,
    "The Annex building":7580,
    "Towers hall":4352
}

def generateDummyData():
    dataToGenerate = int(input("How many rows to generate: "))
    """
        iterate over each key-value pair in a dictionary
        put the key into a variable and the value into another variable
        insert the key (parking lot name) and the value (the IDNum) and boolean (T/F)
        append information to a list
        generate dataframe
    """
    randomSample = random.sample(parkingLots.items(), dataToGenerate)
    print(randomSample)

    for eachIndex in randomSample:
        # Name of the parking lot
        parkingLotName = eachIndex[0]
        print("Parking Lot Name:",parkingLotName)
        parkingLotID = eachIndex[1]
        print("Parking Lot Id:",parkingLotID)
        randomNumber = random.randint(0,1)
        generatedData.append([parkingLotID,parkingLotName,randomNumber])
    print(generatedData)
    generatedData_DF = pd.DataFrame(generatedData, columns=["ParkingID", "ParkingLot", "ParkingOccupany"])
    pd.pandas.set_option('display.max_columns', None)
    print(generatedData_DF)
    transferToCSV(generatedData_DF)


def transferToCSV(dataFrame):
    dataFrame.to_csv('IoTDeviceData.csv')
    

def sendToDB():
    pass


generateDummyData()