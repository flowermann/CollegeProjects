import csv

def read_games(filename):
    games = []
    try:
        with open(filename, newline='', encoding='utf-8') as f:
            reader = csv.reader(f)
            first_line = True
            for row in reader:
                if first_line:
                    first_line = False  # skip header
                else:
                    if len(row) >= 11:
                        games.append([
                            row[0],             # title
                            row[1],             # platform
                            int(row[2]),        # year
                            row[3],             # genre
                            row[4],             # publisher
                            float(row[5]),      # north america sales
                            float(row[6]),      # europe sales
                            float(row[7]),      # japan sales
                            float(row[8]),      # rest of world sales
                            float(row[9]),      # global sales
                            float(row[10])      # rating
                        ])
        return games
    except FileNotFoundError:
        print("Error: games.csv file not found")
        return []

def print_top3_per_platform(games):
    platforms_line = input()
    platforms = [x.strip() for x in platforms_line.split(",")]
    for p in platforms:
        filtered = [g for g in games if g[1] == p]
        filtered.sort(key=lambda x: x[9], reverse=True)  # sort by global sales descending
        print(p)
        for i in range(min(3, len(filtered))):
            print(filtered[i][0])

def console_stats(games):
    sales_threshold = float(input())
    platforms = []
    for row in games[1:]:
        if row[1] not in platforms:
            platforms.append(row[1])

    sales = []
    for platform in sorted(platforms):
        sum_na = 0.0
        sum_eu = 0.0
        sum_jp = 0.0
        sum_ot = 0.0

        for row in games:
            if row[1] == platform:
                counter = 0
                if row[5] >= sales_threshold:
                    counter += 1
                if row[6] >= sales_threshold:
                    counter += 1
                if row[7] >= sales_threshold:
                    counter += 1
                if row[8] >= sales_threshold:
                    counter += 1
                if counter >= 3:
                    sum_na += row[5]
                    sum_eu += row[6]
                    sum_jp += row[7]
                    sum_ot += row[8]
        if sum_na > 0 or  sum_eu > 0 or sum_jp > 0 or sum_ot > 0:
            sales.append([platform,sum_na,sum_eu,sum_jp,sum_ot])

    output_format = []
    for i in sales:
            output_format.append(sales[0] + "\n")
            output_format.append(f"\tNorth America => {i[1]:.2f}\n")
            output_format.append(f"\tEurope => {i[2]:.2f}\n")
            output_format.append(f"\tJapan => {i[3]:.2f}\n")
            output_format.append(f"\tRest of World => {i[4]:.2f}\n")
    output = "\n".join(output_format)
        
    with open("console_stats.txt",'w', encoding='utf-8') as f:
        f.write(output)



games = read_games("games.csv")

if len(games) != 0:
    print_top3_per_platform(games)
    console_stats(games)
