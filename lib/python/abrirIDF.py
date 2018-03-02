from eppy.modeleditor import IDF
import sys
import os
from shutil import copy2
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = sys.argv[1]
archivoIDF = sys.argv[2]
rutaMateriales = sys.argv[3]
nombreRespaldo = "respaldo_"+os.path.basename(archivoIDF)
rutaRespaldo = os.path.join(os.path.dirname(archivoIDF), nombreRespaldo)
if not os.path.exists(rutaRespaldo):    
    copy2(archivoIDF, rutaRespaldo)

IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF)
materiales = idf.idfobjects['MATERIAL']

archivoMateriales = open(rutaMateriales, "r")
listaMateriales = archivoMateriales.read().splitlines()
for linea in listaMateriales:
    for material in materiales:
        if(str(material.Name) == linea):
            #print("Material encontrado " + str(material.Name) + " " + linea)
            #al imprimir mandar valor rangomin rangomax
            print("Thickness", end='%')
            print(material.Thickness, end='%')
            print(material.getrange("Thickness")['minimum'], end='%')
            print(material.getrange("Thickness")['maximum'], end='%')
            print(material.getrange("Thickness")['minimum>'], end='%')
            print(material.getrange("Thickness")['maximum<'])
            '''
            print("Conductivity", end='%')
            print(material.Conductivity, end='%')
            print(material.getrange("Conductivity")['minimum'], end='%')
            print(material.getrange("Conductivity")['maximum'], end='%')
            print(material.getrange("Conductivity")['minimum>'], end='%')
            print(material.getrange("Conductivity")['maximum<'])

            print("Density", end='%')
            print(material.Density, end='%')
            print(material.getrange("Density")['minimum'], end='%')
            print(material.getrange("Density")['maximum'], end='%')
            print(material.getrange("Density")['minimum>'], end='%')
            print(material.getrange("Density")['maximum<'])

            print("Specific Heat", end='%')
            print(material.Specific_Heat, end='%')
            print(material.getrange("Specific_Heat")['minimum'], end='%')
            print(material.getrange("Specific_Heat")['maximum'], end='%')
            print(material.getrange("Specific_Heat")['minimum>'], end='%')
            print(material.getrange("Specific_Heat")['maximum<'])

            print("Thermal Absorptance", end='%')
            print(material.Thermal_Absorptance, end='%')
            print(material.getrange("Thermal_Absorptance")['minimum'], end='%')
            print(material.getrange("Thermal_Absorptance")['maximum'], end='%')
            print(material.getrange("Thermal_Absorptance")['minimum>'], end='%')
            print(material.getrange("Thermal_Absorptance")['maximum<'])


            print("Solar Absorptance", end='%')
            print(material.Solar_Absorptance, end='%')
            print(material.getrange("Solar_Absorptance")['minimum'], end='%')
            print(material.getrange("Solar_Absorptance")['maximum'], end='%')
            print(material.getrange("Solar_Absorptance")['minimum>'], end='%')
            print(material.getrange("Solar_Absorptance")['maximum<'])

            print("Visible Absorptance", end='%')
            print(material.Visible_Absorptance, end='%')
            print(material.getrange("Visible_Absorptance")['minimum'], end='%')
            print(material.getrange("Visible_Absorptance")['maximum'], end='%')
            print(material.getrange("Visible_Absorptance")['minimum>'], end='%')
            print(material.getrange("Visible_Absorptance")['maximum<'])
            print("---")
            '''
            break
            
archivoMateriales.close()       
        
                    
            
'''indicar los variables a optimizar
java captura la salida estandar para saber cuantas variables son
dada una lista de materiales, optimizar thickness'''