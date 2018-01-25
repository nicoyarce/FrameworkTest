from eppy.modeleditor import IDF
import sys
pathnameto_eppy = '../'
sys.path.append(pathnameto_eppy)

archivoIDD = "C:\\EnergyPlusV8-3-0\\Energy+.idd"
archivoIDF = sys.argv[1]

IDF.setiddname(archivoIDD)
idf = IDF(archivoIDF)

materiales = idf.idfobjects['MATERIAL']

for argumento in sys.argv[2:]:
    materiales[0].Thickness = argumento	

idf.save()