import csv

def data(file_name):
    commits = []
    try:
        with open(file_name,'r',encoding='utf-8') as f:
            reader = csv.reader(f)
            for row in reader:
                commits.append(row)
    except FileNotFoundError:
        print("DAT_GRESKA", end='')
        return
    return commits

def group_commit_hash(commits):
    new_commits = []
    for row in commits[1:]:
        commit_hash = row[0]
        file_name = row[1]
        num_add = int(row[2])
        num_del = int(row[3])
        author_id = int(row[4])
        found = False
        for i in range(len(new_commits)):
            if new_commits[i][0] == commit_hash:
                new_commits[i][1].append([file_name,num_add,num_del,author_id])
                found = True
                break
        if not found:
                new_commits.append((commit_hash, [[file_name,num_add,num_del,author_id]]))
    return new_commits

def large_commits(corrected_file):
    line_list = []
    for commit in corrected_file:
        commit_hash = commit[0]
        changes = commit[1]
        if len(changes)>1:
            file = [x[0] for x in changes]
            author_id = changes[0][3]
            num_changes = sum([x[1] + x[2] for x in changes])
            avg_add = sum([x[1] for x in changes])/len(changes)
            avg_del = sum([x[2] for x in changes])/len(changes)
            line = f"{commit_hash}[{author_id}],{{{','.join(file)}}},{num_changes},{avg_add:.2f},{avg_del:.2f}"
            line_list.append(line)
    line_list.sort()
    with open("large_commits.txt", 'w', encoding='utf-8') as f:
        for line in line_list:
            f.write(line + "\n")

def file_changes(corrected_file):
    path = input()
    result_list = []
    found = False
    for commit in corrected_file:
        commit_hash = commit[0]
        changes = commit[1]
        for change in changes:
            file_name, num_add,num_del,author_id = change
            if file_name == path:
                found = True
                result_list.append([author_id,commit_hash,num_add,num_del])
    if not found:
        print("GRESKA", end='')
        return
    result_list.sort(key=lambda x: (x[0],-(x[2]+x[3]), -x[2],x[1]))
    with open("file_changes.log",'w',encoding='utf-8') as f:
        for row in result_list:
            f.write(f"{row[0]},{row[1]},{row[2]},{row[3]}\n")
        
def range_changes(corrected_file):
    try:
        min_and_max = input().split()
        if len(min_and_max)!=2:
            print("GRESKA", end='')
            return
        min_val = int(min_and_max[0])
        max_val = int(min_and_max[1])
        if min_val>max_val:
            print("GRESKA", end='')
            return
    except:
        print("GRESKA", end='')
        return
    end_list = []
    for commit in corrected_file:
        commit_hash = commit[0]
        changes = commit[1]
        total_num_changes = sum([x[1]+x[2] for x in changes])
        if min_val<= total_num_changes <= max_val:
            end_list.append((commit_hash, total_num_changes))
    end_list.sort(key=lambda x: x[0])
    with open("range_changes.csv", 'w', encoding='utf-8') as f:
         for commit_hash, total in end_list:
            f.write(f"{commit_hash},{total}\n")

file_data = data("linux_commits.csv")
if file_data is None:
    pass
else:
    corrected_file = group_commit_hash(file_data)
    if corrected_file is None:
        pass
    else:
        l_commits = large_commits(corrected_file)
        f_changes = file_changes(corrected_file)
        r_changes = range_changes(corrected_file)
