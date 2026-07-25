def tezine():
    unos= input().split()
    tezina=[]
    try:
        for t in unos:
            broj = int(t)
            if broj<1 or broj>5:
                return None
            
            else:
                tezina.append(broj)
    except: 
        return 
    return tezina


def sobe():
    lista_soba = []
    while True:
        soba = input().split(",")
        try:
            naziv = str(soba[0])
            broj_igraca = str(soba[1])
            tezina = int(soba[2])
            soba = [naziv, broj_igraca, tezina]
            lista_soba.append(soba)
        except:
            break
    return lista_soba

print(tezine())
print(sobe())