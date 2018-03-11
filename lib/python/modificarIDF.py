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

modificaciones = []

for argumento in sys.argv[4:]:
    modificaciones.append(argumento)

archivoMateriales = open(rutaMateriales, "r")
listaMateriales = archivoMateriales.read().splitlines()
i = 0
for linea in listaMateriales:
    for material in materiales:
        if(str(material.Name) == linea):
            material.Thickness = modificaciones[i]
            material.Conductivity = modificaciones[i + 1]
            material.Density = modificaciones[i + 2]
            material.Specific_Heat = modificaciones[i + 3]
            material.Thermal_Absorptance = modificaciones[i + 4]
            material.Solar_Absorptance = modificaciones[i + 5]
            material.Visible_Absorptance = modificaciones[i + 6]
    i += 7


archivoMateriales.close()
idf.save()