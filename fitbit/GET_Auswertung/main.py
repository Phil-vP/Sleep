import requests
import datetime
import time as timeLib

allDates = [Dates]
allResponses = []
USER = "User"
HEADER = {"authorization":
                "Key"
         }

lookupTable = {
    "light": "L",
    "deep": "T",
    "wake": "W",
    "rem": "R"
}

countShort = True

def getRequests_single():
    print("getting Bodies")
    for date in allDates:
        URL = "https://web-api.fitbit.com/1.2/user/" + USER + "/sleep/date/" + date + ".json"

        r = requests.get(url = URL, headers = HEADER)

        print(r)
        allResponses.append(r.json())
        print(r.json())

    print(allResponses)

def getRequests_total():
    global allResponses
    URL = "https://web-api.fitbit.com/1.2/user/" + USER + "/sleep/list.json"
    PARAMS = {
        "limit": "50",
        "offset": "0",
        "sort": "desc",
        "beforeDate": "2021-04-12"
    }

    r = requests.get(url=URL, params=PARAMS, headers=HEADER)
    print(r)
    rJSON = r.json()

    allResponses = rJSON["sleep"]

    print("-----------")

    for key in list(allResponses[3].keys()):
        print(key + ": " + str(allResponses[3][key]))

def evaluate_single():
    for resp in allResponses:
        sleeps = resp["sleep"]
        for sleep in sleeps:
            evalSleep(sleep)

def evaluate_total():
    for resp in allResponses:
        evalSleep(resp)

def evalSleep(sleep):
    print("----------------")
    date = datetime.datetime.strptime(sleep["dateOfSleep"], "%Y-%m-%d").strftime("%Y_%m_%d")
    print(date)
    sleepData = sleep["levels"]["data"]
    firstLevel = sleepData[0]["level"]
    print("First Level: " + sleepData[0]["level"])
    if firstLevel == "restless" or firstLevel == "asleep" or firstLevel == "awake":
        print("Continue")
        return

    sleepMap = {}
    for sle in sleepData:
        # Sleeping Phases
        time_str = sle["dateTime"]
        time_str = time_str.replace("T", " ")
        time = datetime.datetime.strptime(time_str, "%Y-%m-%d %H:%M:%S.%f")
        
        for i in range(int(sle["seconds"]/30)):
            sleepMap[time.strftime("%H:%M:%S")] = lookupTable[sle["level"]]
            time += datetime.timedelta(seconds=30)

    if countShort:
        shortData = sleep["levels"]["shortData"]
        for sle in shortData:
            # Sleeping Phases short

            time_str_short = sle["dateTime"]
            time_str_short = time_str_short.replace("T", " ")
            time_short = datetime.datetime.strptime(time_str_short, "%Y-%m-%d %H:%M:%S.%f")

            for i in range(int(sle["seconds"]/30)):
                sleepMap[time_short.strftime("%H:%M:%S")] = lookupTable[sle["level"]]
                time_short += datetime.timedelta(seconds=30)

    filename = "B:\\Projekte\\Schlaf\\fitbit\\Auswertung\\fitbit_" + date + "_" + str(round(timeLib.time() * 1000) % 100000) + ".csv"
    print(filename)
    with open(filename, "w") as outFile:
        for timestamp in list(sleepMap.keys()):
            outFile.write(timestamp + " , " + sleepMap[timestamp] + "\n")


def single():
    getRequests_single()
    evaluate_single()

def total():
    getRequests_total()
    evaluate_total()


if __name__ == '__main__':
    # single()
    total()
