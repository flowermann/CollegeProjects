import csv

def load_commits(file_name):
    commits = []
    try:
        with open(file_name, 'r', encoding='utf-8') as f:
            reader = csv.reader(f)
            next(reader)  # preskoci zaglavlje
            for row in reader:
                commit_hash = row[0]
                file_name = row[1]
                num_add = int(row[2])
                num_del = int(row[3])
                author_id = int(row[4])

                found = False
                for i in range(len(commits)):
                    if commits[i][0] == commit_hash:
                        commits[i][1].append([file_name, num_add, num_del, author_id])
                        found = True
                        break
                if not found:
                    commits.append((commit_hash, [[file_name, num_add, num_del, author_id]]))
        return commits
    except:
        print("DAT_GRESKA", end='')
        

def write_large_commits(commits):
    lines = []
    for commit in commits:
        commit_hash = commit[0]
        changes = commit[1]
        if len(changes) > 1:
            files = [entry[0] for entry in changes]
            author_id = changes[0][3]
            num_changes = sum(entry[1] + entry[2] for entry in changes)
            avg_add = sum(entry[1] for entry in changes) / len(changes)
            avg_del = sum(entry[2] for entry in changes) / len(changes)
            line = f"{commit_hash}[{author_id}],{','.join(files)},{num_changes},{avg_add:.2f},{avg_del:.2f}"
            lines.append(line)
    lines.sort()
    with open("large_commits.txt", "w", encoding="utf-8") as f:
        for line in lines:
            f.write(line + "\n")

def write_file_changes(commits):
    path = input().strip()
    result = []
    found = False
    for commit in commits:
        commit_hash = commit[0]
        changes = commit[1]
        for change in changes:
            file_name, num_add, num_del, author_id = change
            if file_name == path:
                found = True
                result.append([author_id, commit_hash, num_add, num_del])
    if not found:
        print("DAT_GRESKA", end='')
    result.sort(key=lambda x: (x[0], -(x[2]+x[3]), -x[2], x[1]))
    with open("file_changes.log", "w", encoding="utf-8") as f:
        for row in result:
            f.write(f"{row[0]},{row[1]},{row[2]},{row[3]}\n")

def write_range_changes(commits):
    try:
        min_val = int(input())
        max_val = int(input())
    except:
        return
    result = []
    for commit in commits:
        commit_hash = commit[0]
        changes = commit[1]
        total = sum(entry[1] + entry[2] for entry in changes)
        if min_val <= total <= max_val:
            result.append((commit_hash, total))
    result.sort()
    with open("range_changes.csv", "w", encoding="utf-8") as f:
        for commit_hash, total in result:
            f.write(f"{commit_hash},{total}\n")

# Glavni deo
commits = load_commits("linux_commits.csv")
if commits is None:
    pass
else:
    write_large_commits(commits)
    write_file_changes(commits)
    write_range_changes(commits)
