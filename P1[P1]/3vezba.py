import csv 

def data(filename):
    games = []
    g = []
    try:
        with open(filename,'r',encoding='utf-8') as f:
            reader = csv.reader(f)
            for row in reader:
                g.append(row)
            g.remove(g[0])
            for i in g:
                i[2] = int(i[2])
                i[5] = float(i[5])
                i[6] = float(i[6])
                i[7] = float(i[7])
                i[8] = float(i[8])
                i[9] = float(i[9])
                i[10] = float(i[10])
                games.append(i)
    except FileNotFoundError:
        print("DAT_GRESKA", end='')
        return
    return games

def platform_sales(games):
    platform_search = input().split(",")
    output_format = []
    for platform in platform_search:
        platform = platform.strip()
        filtered_platforms = []
        for row in games:
            if row[1] == platform:
                filtered_platforms.append(row)
        filtered_platforms.sort(key=lambda x: float(x[9]), reverse=True)
        top3 = [game[0] for game in filtered_platforms[:3]]
        output_format.append(f"{platform} => {"|".join(top3)}")
    output = "\n".join(output_format)
    print(output,end='')

def console_stats(games):
    threshold = float(input())
    platforms = []
    
    for row in platforms:
        if row[1] not in platforms:
            platforms.append(row[1])
    
    sales = []
    for p in platforms:
        na_sum = 0.0
        eu_sum = 0.0
        jp_sum = 0.0
        row_sum = 0.0
        for row in games:
            if row[1]==p:
                na_sum+=row[5]
                eu_sum+=row[6]
                jp_sum+=row[7]
                row_sum+=row[8]
        sales.append([p,na_sum,eu_sum,jp_sum,row_sum])

    filtered = []
    for i in sales:
        count = 0
        for val in i[1:]:
            if val>=threshold:
                count+=1
        if count>=3:
            filtered.append(i)
    
    filtered.sort(key=lambda x: x[0])

    output_format = []
    for p in filtered:
        output_format.append(p[0])
        output_format.append(f"\tNorth America: {p[1]:.2f}")
        output_format.append(f"\tEurope: {p[2]:.2f}")
        output_format.append(f"\tJapan: {p[3]:.2f}")
        output_format.append(f"\tRest of World: {p[4]:.2f}")
        
    with open("console_stats.txt",'w', encoding='utf-8') as f:
        f.write("\n".join(output_format))
                             
def company_most_popular(games):
    min_rating = float(input())
    publishers = []
    for row in games:
        if row[4] not in publishers:
            publishers.append(row[4])

    companies = []
    for publisher in publishers:
        filtered_games = []
        for row in games:
            if row[4] == publisher and row[10]>=min_rating:
                filtered_games.append([row[0],row[10]])
        if len(filtered_games)!=0:
            filtered_games.sort(key=lambda x: x[1],reverse=True)
            companies.append([publisher,filtered_games])
    companies.sort(key=lambda x: x[0])
    output_format = []
    for company in companies:
        game_names = [game[0] for game in company[1]]
        output_format.append(f"{company[0]} => {",".join(game_names)}")
    with open("company_most_popular.txt", 'w', encoding='utf-8') as f:
        f.write("\n".join(output_format))

file_data = data("games.csv")
if file_data:
    platform_sales(file_data)
    console_stats(file_data)
    company_most_popular(file_data)