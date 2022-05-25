import requests

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


with open('token.txt', 'r') as f:
    token = f.read().strip()


api = Curseforge_api(token)

# mod_json = api.get_mod_info(406360)
mod_json = api.get_mods_fileids_json([3651868, 3723162, 3559400])

import pyjsonviewer
pyjsonviewer.view_data(json_data=mod_json)






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


