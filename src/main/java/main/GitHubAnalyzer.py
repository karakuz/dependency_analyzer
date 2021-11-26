from pydriller import Repository
import pprint
import json

pp = pprint.PrettyPrinter(indent=4)
commits = []
for commit in Repository('https://github.com/karakuz/Test-Project',
                         only_modifications_with_file_types=['.java']).traverse_commits():

    changes = []
    for file in commit.modified_files:
        if file.filename.endswith(".java"):
            if file.new_path:
                changes.append(file.new_path[3:])

    commitObj = {
        "commit_message": commit.msg,
        "committer": commit.author.email,
        "changes": changes
    }

    hash = commit.hash

    commits.append(commitObj)

commits_ = json.dumps({"commits": commits})
print(commits_)

classInfos = json.load(open('../../../../classInfos.json'))["classes"]
print(classInfos)

with open('../../../../classInfos.json', 'w', encoding='utf-8') as f:
    json.dump({"classes": classInfos, "commits": commits}, f, ensure_ascii=False, indent=4)