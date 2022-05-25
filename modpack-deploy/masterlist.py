import requests
import json
from time import sleep

class Curseforge_api():
    def __init__(self, api_key):
        self._api_key = api_key
        self.base_url = "https://api.curseforge.com/"
        self.headers = {'Content-Type': 'application/json', 'Accept': 'application/json', 'x-api-key': self._api_key}


    def get_mod_json(self, mod_id, file_id=None):
        if file_id is not None:
            url = self.base_url + "v1/mods/" + str(mod_id) + "/files/" + str(file_id)
        else:
            url = self.base_url + "v1/mods/" + str(mod_id)

        response = requests.get(url, headers=self.headers)

        if response.status_code != 200:
            raise Exception(str(response.status_code))
        return response.json()

    def get_mods_fileids_json(self, file_ids):
        url = self.base_url + "v1/mods/files"
        data = {"fileIds": file_ids}

        response = requests.post(url, data=str(data), headers=self.headers)

        if response.status_code != 200:
            raise Exception(str(response.status_code))
        return response.json()

    def get_mods_modids_json(self, mod_ids):
        url = self.base_url + "v1/mods"
        data = {"modIds": mod_ids}

        response = requests.post(url, data=str(data), headers=self.headers)

        if response.status_code != 200:
            raise Exception(str(response.status_code))
        return response.json()


with open('token.txt', 'r') as f:
    token = f.read().strip()


api = Curseforge_api(token)

# mod_json = api.get_mod_info(406360)

mods_list = {'data': []}
l2 = []

import pyjsonviewer

for i in range(0, 1000000 - 1000, 1000):
    l2 = []
    for j in range(0, 1000):
        l2.append(i + j)

    mod_json = api.get_mods_modids_json(l2)

    print(f"Mods Found i = {i}: {len(mod_json['data'])}")
    mods_list['data'] += mod_json['data']
    sleep(3)

print(f"Total size: {len(mods_list['data'])}")

print(f"Saving to file...")
with open('masterlist.json', 'w') as f:
    json.dump(mods_list, f, indent=4)
print(f"Done!")


# mod_json = api.get_mods_fileids_json([3651868, 3723162, 3559400])
# mod_json = api.get_mods_modids_json([406360, 238222, 363703])


# import pyjsonviewer
# pyjsonviewer.view_data(json_data=mod_json)






### TESTING STUFF BELLOW ###

# headers = {
#   'Content-Type': 'application/json',  
#   'Accept': 'application/json',
#   'x-api-key': token
# }

# r = requests.get('https://api.curseforge.com/v1/mods/406360/files/3651868/', headers = headers)
# r = requests.get('https://api.curseforge.com/v1/mods/406360', headers = headers)


# data = {
#   "modIds": [
#     406360,
#   ]
# }

# data = {
#   "fileIds": [
#     0,
#   ]
# }

# print(data)

# r = requests.post('https://api.curseforge.com/v1/mods/files', data=str(data), headers=headers)

# print(r)

# import pyjsonviewer
# pyjsonviewer.view_data(json_data=r.json())


