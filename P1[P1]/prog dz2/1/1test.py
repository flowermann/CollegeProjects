import csv 

def upis(file_name):
    transfers = []
    try:
        with open(file_name, 'r', encoding='utf-8',) as f:
            reader= csv.reader(f)
            for row in reader:
                transfers.append(row)
    except FileNotFoundError:
        print("DAT_GRESKA", end='')
        return 
    return transfers

        
def clubs(transfers):
    club_search = input().split(',')
    club_search = list(set(club_search))
    all_clubs = [row[4] for row in transfers]
    for club in club_search:
        if club not in all_clubs:
            print("DAT_GRESKA", end='')
            pass
    with open ("clubs_stat.txt", "w", encoding='utf-8') as f:
        for club in club_search:
            f.write(f"{club}\n")
            for row in transfers:
                if row[4] == club:
                    f.write(f"{row[0]} {row[2]} {row[5]}\n")

def league(transfers):
    statistics = []
    for row in transfers[1:]:
        league = row[7]
        price = int(row[6])
        found = False
        for i in statistics:
            if i[0] == league:
                i[1] += 1
                i[2] += price
                found = True
                break
        if not found:
            statistics.append([league,1,price])
    
    with open("league_stat.txt", "w", encoding='utf-8') as f:
        for write in sorted(statistics, key=lambda x: x[0]):
            f.write(f"{write[0]},{write[1]},{write[2]}\n")


def position(transfers):
    position_search = input().split(",")
    info_list = []
    all_positions = [row[2] for row in transfers]
    for pos in position_search:
        if pos not in all_positions:
            print("DAT_GRESKA", end='')
            return None
    for row in transfers[1:]:
        if row[2] in position_search:
            info_list.append([row[7],row[8],row[0]])
    
    result_list = []
    for league,season,player in info_list:
        found = False
        for i in result_list:
            if i[0] == league and i[1] == season:
                i[2].append(player)
                found = True
                break
        if not found:
            result_list.append([league,season,[player]])

    with open("pos_stat.txt",'w',encoding='utf-8') as f:
        current_league = ''
        for league,season,player in sorted(result_list, key=lambda x:(x[0],x[1])):
            if league != current_league:
                f.write(f"{league}\n")
                current_league = league
            f.write(f"{season},{','.join(player)}\n")







file_data = upis("football_transfers.csv")
if file_data is None:
    pass
else:
    clubs_filtered = clubs(file_data)
    leagues_filtered = league(file_data)
    positions_filtered = position(file_data)

            

