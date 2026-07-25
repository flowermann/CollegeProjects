def vezbe():
    try:
        n = int(input("Unesite broj vezbi:" ))
        listavezbi= []
        for i in range(n):
            vezba = input().split(',')
            vezba[2] = int(vezba[2])
            vezba[3] = int(vezba[3])
            listavezbi.append(vezba)
    except:
        return
    return listavezbi


def zelje():
    zelja = input().split(',')
    zelja[0]=int(zelja[0])
    return zelja

def filter_misici(vezba, zelja):
    filtriranje1 = []
    grupamisica = zelja[1]
    for vezba in listavezbi:
        if vezba[1]==grupamisica:
            filtriranje1.append(vezba)
    return filtriranje1

def filter_intenzitet(vezba,zelja):
    filtriranje2 = []
    intenzitet = zelja[2]
    for vezba in listavezbi:
        if intenzitet=="slab" and vezba[3]<60:
            filtriranje2.append(vezba)
        elif intenzitet=="srednji" and 60<=vezba[3]<100:
            filtriranje2.append(vezba)
        elif intenzitet=="jak" and vezba[3]>=100:
            filtriranje2.append(vezba)
    return filtriranje2


v = vezbe()
z = zelje()
fm = filter_misici(v,z)
fi = filter_intenzitet(v,z)
print(fm)
print(fi)