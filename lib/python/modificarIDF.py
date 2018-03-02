from eppy.modeleditor import IDF
import sys
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = sys.argv[1]
archivoIDF = sys.argv[2]
rutaMateriales = sys.argv[3]
IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF)
materiales = idf.idfobjects['MATERIAL']

archivoMateriales = open(rutaMateriales, "r")
listaMateriales = archivoMateriales.read().splitlines()
i = 0;
for linea in listaMateriales:
    for material in materiales:
        if(str(material.Name) == linea):
            material.Thickness = sys.argv[i + 4]
            '''
            material.Conductivity = sys.argv[i + 5]
            material.Density = sys.argv[i + 6]
            material.Specific_Heat = sys.argv[i + 7]
            material.Thermal_Absorptance = sys.argv[i + 8]
            material.Solar_Absorptance = sys.argv[i + 9]
            material.Visible_Absorptance = sys.argv[i + 10]
            '''
            break
    i += 1
archivoMateriales.close() 

idf.save()