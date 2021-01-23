
import glob
from PIL import Image



def cropImages():
    print("Getting images")
    path = "B:\\Projekte\\Schlaf\\Auswertung\\"
    allImages = glob.glob(path + "*.JPG")

    for img in allImages:
        image = Image.open(img)
        filename = img.split("\\")[-1]

        if "crop" in filename:
            continue

        print(filename)
        print(img)
        print(image.size)
        print()

        width, height = image.size
        left = 0
        right = width
        top = 450
        bottom = 500
        im1 = image.crop((left, top, right, bottom))
        # im1.show()
        im1.save(path + "crop_" + filename, "JPEG")





def testImages():
    path = "B:\\Projekte\\Schlaf\\Auswertung\\"
    filename = "VP01_N1.JPG"
    image = Image.open(path + filename)
    width, height = image.size
    print(image.size)
    left = 0
    right = width
    top = 450
    bottom = 500
    im1 = image.crop((left, top, right, bottom))
    im1.show()
    # im1.save(path + "crop_" + filename, "JPEG")



def printFilenames():
    path = "B:\\Projekte\\Schlaf\\Auswertung\\"
    allImages = glob.glob(path + "*.JPG")

    for i in allImages:
        print("put(\"" + i.split("\\")[-1] + "\", new Pair<String, String>(\"\", \"\");")



if __name__== "__main__":
    print("Cropping images")
    cropImages()
