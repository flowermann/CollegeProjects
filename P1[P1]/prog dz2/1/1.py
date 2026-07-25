import csv 

def upis(file_name):
    transfers = []
    try:
        with open(file_name, 'r', encoding='utf-8',) as f:
            reader= csv.reader(f)
            for row in reader:
                transfers.append(row)
    except FileNotFoundError:
        print("DAT_GRESKA",end='')
        return 
    return transfers

        
def clubs(transfers):
    club_search = input().split(',')
    all_clubs = [row[4] for row in transfers]
    for club in club_search:
        if club not in all_clubs:
            print("GRESKA",end='')
            return 
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
    position_search = input().split()
    all_positions = [row[2] for row in transfers[1:]]

    for pos in position_search:
        if pos not in all_positions:
            print("GRESKA", end='')
            return

    data_list = []
    for row in transfers[1:]:
        if row[2] in position_search:
            data_list.append([row[7], row[8], row[0]])

    league_order = []
    for i in data_list:
        league = i[0]
        if league not in league_order:
            league_order.append(league)

    result = []
    for league in league_order:
        season_players = []
        for r in data_list:
            if r[0] == league:
                season = r[1]
                player = r[2]
                found = False
                for group in season_players:
                    if group[0] == season:
                        group[1].append(player)
                        found = True
                        break
                if not found:
                    season_players.append([season, [player]])
        result.append([league, season_players])

    with open("pos_stat.txt", "w", encoding="utf-8") as f:
        for league, season_players in result:
            f.write(f"{league}\n")
            for season, players in sorted(season_players, key=lambda x: x[0]):
                sorted_players = sorted(players)
                f.write(f"{season},{','.join(sorted_players)}\n")



file_data = upis("football_transfers.csv")
if file_data is None:
    pass
else:
    clubs_filtered = clubs(file_data)
    leagues_filtered = league(file_data)
    positions_filtered = position(file_data)
  

            

