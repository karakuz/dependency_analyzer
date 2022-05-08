from pydriller import Repository
import pprint
import json
import sys
import requests
import urllib.parse

JSON = json.load(open('../../../../classInfos.json'))
githubURL = JSON["githubURL"]

commits = []
for commit in Repository(githubURL, only_modifications_with_file_types=['.java']).traverse_commits():
    changes = []
    for file in commit.modified_files:
        if file.filename.endswith(".java") and file.new_path:
            filePath = file.new_path
            if "src" in file.new_path:
                filePath = filePath[filePath.index("src")+4:]
            filePath = filePath.replace("\\", ".")
            changes.append(filePath[:-5])

    commitObj = {
        "hash": commit.hash,
        "commit_message": commit.msg,
        "committer": commit.author.email,
        "changes": changes
    }
    commits.append(commitObj)

#commits_ = json.dumps({"commits": commits})
#print(commits_)


with open('../../../../classInfos.json', 'w', encoding='utf-8') as f:
    json.dump({"githubURL": githubURL, "commits": commits}, f, ensure_ascii=False, indent=4)

sys.exit()




githubRepoInfo = githubURL.split('https://github.com/')[1].split('/')
githubUser = githubRepoInfo[0]
githubUserRepo = githubRepoInfo[1]

githubAPI_BaseURL = "https://api.github.com/repos"

githubAPI_RepoBaseURL = f"{githubAPI_BaseURL}/{githubUser}/{githubUserRepo}"
githubAPI_RepoLabelsURL = f"{githubAPI_RepoBaseURL}/labels"
githubAPI_RepoIssuesURL = f"{githubAPI_RepoBaseURL}/issues"


page = 1
labelsNamedBug = []
while True:
    url = f"{githubAPI_RepoLabelsURL}?per_page=100&page={page}"
    response = requests.get(url).json()
    for label in response:
        labelName = label['name']
        if "bug" in labelName.lower():
            labelsNamedBug.append(labelName)
    if len(response) == 100:
        page = page + 1
        continue
    break

page = 1
issuesContainingBugLabel = []
for labelNamedBug in labelsNamedBug:
    encodedLabelName = urllib.parse.quote(labelNamedBug)
    while True:
        url = f"{githubAPI_RepoIssuesURL}?state=closed&labels={encodedLabelName}&per_page=100&page={page}"
        response = requests.get(url).json()
        for issueObject in response:
            if "pull_request" in issueObject:
                issuesContainingBugLabel.append(issueObject)
        if len(response) == 100:
            page = page+1
            continue
        break


with open('../../../../issueData.json', 'w', encoding='utf-8') as f:
    json.dump(issuesContainingBugLabel, f, ensure_ascii=False, indent=4)

sys.exit()


