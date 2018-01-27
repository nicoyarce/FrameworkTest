from eppy.results import readhtml
import sys
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

lista_de_valores = []

fname = 'C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\simulacion\\eplustbl.htm'  # archivo html valor
filehandle = open(fname, 'r').read()  # get a file handle to the html file
htables = readhtml.titletable(filehandle) # reads the tables with their titles

valores = []
valores.append(htables[0][1][1][1])
valores.append(htables[0][1][3][1])
valores.append(htables[17][1][2][5])
valores.append(htables[17][1][3][4])
valores.append(htables[11][1][1][1])
valores.append(htables[6][1][2][5])

lista_de_valores.append(valores)

header = []
# TITLE Total Energy (kWh) / Total Site Energy
attrib = ('[' + str(htables[0][1][0][1]) + '/' + str(htables[0][1][1][0]) + ']')
header.append(attrib)
# TITLE Total Energy (kWh) / Total Source Energy
attrib1 = ('[' + str(htables[0][1][0][1]) + '/' + str(htables[0][1][3][0]) + ']')
header.append(attrib1)
# TITLE District Heating [W] / Heating
attrib2 = ('[' + str(htables[17][1][0][5]) + '/' + str(htables[17][1][2][0]) + ']')
header.append(attrib2)
# TITLE District Cooling [W] / Cooling
attrib3 = ('[' + str(htables[17][1][0][4]) + '/' + str(htables[17][1][3][0]) + ']')
header.append(attrib3)
# TITLE Facility(Hours) / Time Setpoint Not met  during occupied heating.
attrib4 = ('[' + str(htables[11][1][0][1]) + '/' + str(htables[11][1][1][0]) + ']')
header.append(attrib4)
# TITLE District Heating Intensity [kWh/m2] / HVAC
attrib5 = ('[' + str(htables[6][1][0][5]) + '/' + str(htables[6][1][2][0]) + ']')
header.append(attrib5)

#print por la salida estandar para que java capture
for valorEnergetico in valores:
    print(valorEnergetico)

# GENERA ARCHIVO .TXT
outfile = open('C:\\Users\\Usuario\\Downloads\\eppy-0.5.46\\simulacion\\values_file.txt', 'w')  # Indicamos el valor 'w'.
outfile.write("\t")

for z in range(0, len(header)):        
    outfile.write(" | ")
    outfile.write('{:^25}'.format(str(header[z])))
    
outfile.write('\n')

for m in range(0, len(lista_de_valores)):
    outfile.write("\n")        
    #outfile.write('{:^15}'.format(str(lista_carpeta_idf[m])))
    #print(str(lista_carpeta_idf[m]))
    outfile.write("\t")
    for i in range(0, len(valores)):
        outfile.write(" | ")
        outfile.write("\t")                  
        outfile.write('{:^11}'.format(str(lista_de_valores[m][i])))        
        outfile.write("\t")
outfile.close()
   
