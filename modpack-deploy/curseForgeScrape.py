from selenium import webdriver
from selenium.webdriver.common.by import By
from os.path import exists
import asyncio
import datetime

class Collector:
    def __init__(self, addresses, fileIds):
        self.addresses = addresses   #Should be a list of web addresses for mods
        self.fileId = fileIds
        self.links = []

    async def isLoaded(self, driver):
        loaded = False
        while loaded == False:
            numRows = len(driver.find_elements(By.XPATH, "/html/body/div/main/div/div/section/div/div/div/section/section/article/div/div/a"))
            if(numRows == 2):
                loaded = True
            await asyncio.sleep(1)
        return True

    async def scrapeLinks(self):
        print(datetime.datetime.now())    #For debugging
        options = webdriver.ChromeOptions()
        options.add_argument("--disable-dev-shm-usage")
        options.add_argument("--disable-logging")
        options.add_argument("--log-level=OFF")
        #options.add_argument("--headless")    #This appears to be causing issues for curseForge
        driver = webdriver.Chrome(options=options)

        for i in range(len(self.addresses)):
            driver.get(self.addresses[i])    #Load webpage

            # Wait for page to load fully
            await self.isLoaded(driver)

            #Get text from field and create link
            fileInfo = driver.find_element(By.XPATH, "/html/body/div/main/div/div/section/div/div/div/section/section/article/div/div/a[1]").text

            print(fileInfo)    #For debugging

            #Replace spaces in fileInfo with '+'
            fileInfo = fileInfo.replace(' ', '+')

            #Create correct link
            link = "https://media.forgecdn.net/files/"
            link += (self.fileId[i])[0:4] + "/"
            link += (self.fileId[i])[4:7] + "/"
            link += fileInfo

            #Append corrected link to links List
            self.links.append(link)
        
        #Return the list of links
        print(self.links)
        return self.links

c = Collector(['https://www.curseforge.com/minecraft/mc-mods/mana-and-artifice/files/3651868'], ['3651868'])

asyncio.run(c.scrapeLinks())