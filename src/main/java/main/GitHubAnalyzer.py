from pydriller import Repository
import pprint
import json

JSON = json.load(open('../../../../classInfos.json'))
githubURL = JSON["githubURL"]
classInfos = JSON["classes"]

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
        "commit_message": commit.msg,
        "committer": commit.author.email,
        "changes": changes
    }

    hash = commit.hash
    commits.append(commitObj)

commits_ = json.dumps({"commits": commits})
print(commits_)

print(classInfos)

with open('../../../../classInfos.json', 'w', encoding='utf-8') as f:
    json.dump({"classes": classInfos, "commits": commits}, f, ensure_ascii=False, indent=4)