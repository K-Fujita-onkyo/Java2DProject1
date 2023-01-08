import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RoadVTKFile {

	private String filename;
	private String[] contents;
	private CoolWarmColor[] csvFile;

	public int numberOfPoints;
	public double maxDistance;
	public double[][] pointLocations;
	public int numberOfStructure;
	public int[][] dataStructures;
	public int[] structureTypes;
	public double maxScalars;
	public CoolWarmColor[] pointColors;
    public CoolWarmColor[] lineColors;

	public static final double INF = 9999999;

	RoadVTKFile(String filename){
		this.filename = filename;
	}

	void setUp(){

		maxDistance = -INF;
		maxScalars = -INF;

        System.out.print("Set up vtk file...");

		//find the path of file
		Path vtkPath = Paths.get(filename);

		//road the content of vtk file.
		try {
			contents = Files.readString(vtkPath).split("\n");
			//System.out.println("the number of lines: " + contents.length);//for debug	
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		//open csv file
		Path csvPath = Paths.get("CoolWarmFloat257.csv");
		try {
		
			String[] csvStrFile = Files.readString(csvPath).split("\n");
			csvFile = new CoolWarmColor[csvStrFile.length - 1];

			for(int i = 0; i < csvFile.length; i++){
				String[] colorData = csvStrFile[i+1].split(",");
				csvFile[i] = new CoolWarmColor(Double.parseDouble(colorData[0]), Double.parseDouble(colorData[1]), Double.parseDouble(colorData[2]), Double.parseDouble(colorData[3]));
			}
		
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		//find Dataset line
		boolean flagPoint = false;
		boolean flagStructure = false;
		boolean flagType = false;
		boolean flagColor = false;
		for(int i = 0; i < contents.length; i++){
			if(contents[i].indexOf("POINTS")!=-1){
				flagPoint = true;
				setPoints(i);
			}
			if(contents[i].indexOf("CELLS")!=-1){
				flagStructure = true;
				setStructures(i);
			}
			if(contents[i].indexOf("CELL_TYPES")!=-1){
				flagType =true;
				setTypes(i);
			}
			if(contents[i].indexOf("POINT_DATA")!=-1){
				flagColor =true;
				setColors(i);
			}
		}

		if(!flagPoint||!flagStructure||!flagType||!flagColor){
			System.out.println("Couldn't read the vtk file");
		}

        System.out.println("finished.");

	}

	void setPoints(int lineNumber){

		String[] pointInfo = contents[lineNumber].split(" ");
		numberOfPoints = Integer.parseInt(pointInfo[1]);
		pointLocations = new double[numberOfPoints][3];

		//System.out.println("the number of points: " + numberOfPoints);//for debug

		for(int i = 0; i < numberOfPoints; i++){

			String[] pointData = contents[lineNumber + i + 1].split(" ");//the data of the point "i" is in line "lineNumber + i + 1" of contents.


			for(int j = 0; j < 3; j++){
				pointLocations[i][j] = Double.parseDouble(pointData[j]);
				if(pointLocations[i][j] > maxDistance) maxDistance = pointLocations[i][j];
				else if(-pointLocations[i][j] > maxDistance) maxDistance = -pointLocations[i][j];
				//System.out.print(pointLocations[i][j] + " ");
			}

			//System.out.println();
		}
	}

	void setStructures(int lineNumber){

		String[] structureInfo = contents[lineNumber].split(" ");
		numberOfStructure = Integer.parseInt(structureInfo[1]);

		//System.out.println("the number of structures: " + numberOfStructure);//for debug

		dataStructures = new int[numberOfStructure][];

		for(int i = 0; i < numberOfStructure; i++){

			String[] data = contents[lineNumber + i + 1].split(" ");
			dataStructures[i] = new int[data.length - 1]; //first data is the number of structure.

			for(int j = 2; j < data.length; j++){
				dataStructures[i][j-2] = Integer.parseInt(data[j]);
				//System.out.print(dataStructures[i][j-2] +  " ");
			}
			//System.out.println();
		}
	}

	void setTypes(int lineNumber){
		String[] typeInfo = contents[lineNumber].split(" ");
		int numberOfCells = Integer.parseInt(typeInfo[1]);

		//System.out.println("the number of types: " + numberOfCells);

		structureTypes = new int[numberOfCells];

		for(int i = 0; i < numberOfCells; i++){
			structureTypes[i] = Integer.parseInt(contents[lineNumber + i + 1]);
			//System.out.print(structureTypes[i] + " ");
		}
		//System.out.println();
	}

	void setColors(int lineNumber){

		//new
		String[] typeInfo = contents[lineNumber].split(" ");
		int numberOfColors = Integer.parseInt(typeInfo[1]);

		pointColors = new CoolWarmColor[numberOfColors];

		for(int i = 0; i < numberOfColors; i++){
			pointColors[i] = new CoolWarmColor(Double.parseDouble(contents[lineNumber + i + 3]));
			if(pointColors[i].scalar > maxScalars) maxScalars = pointColors[i].scalar;
		}

		for(int i = 0; i < numberOfColors; i++){

			//interpolation

			pointColors[i].scalar /= maxScalars;

			for(int j = 0; j < csvFile.length; j++){
				if(pointColors[i].scalar >= 1.0){
					pointColors[i] = csvFile[csvFile.length-1];
					break;
				}
				if(pointColors[i].scalar >= csvFile[j].scalar && pointColors[i].scalar < csvFile[j+1].scalar){
					pointColors[i].interpolationColor(csvFile[j], csvFile[j+1]);
					break;
				}
			}
			
			//System.out.println(i+ "i" + pointColors[i].scalar + " " + pointColors[i].R + " " + pointColors[i].G + " " + pointColors[i].B); 
		}
	}

    void calcLineColors(double lineScalars[]){
        
        lineColors = new CoolWarmColor[lineScalars.length];

        for(int i = 0; i < lineScalars.length; i++){

			lineColors[i] = new CoolWarmColor(lineScalars[i]);

            for(int j = 0; j < csvFile.length; j++){
    
                if(lineColors[i].scalar == 1.0){
                    lineColors[i] = csvFile[csvFile.length-1];
                    break;
                }
                if(lineColors[i].scalar >= csvFile[j].scalar && lineColors[i].scalar < csvFile[j+1].scalar){
                    lineColors[i].interpolationColor(csvFile[j], csvFile[j+1]);
                    break;
                }
            }

        }

    }

    void transformPoints(int sizeX, int sizeY){

        double times;

		if(maxDistance<0) maxDistance = (-1) * maxDistance;

        if(sizeX < sizeY) times = sizeX/(maxDistance*2.5);
        else times = sizeY/(maxDistance*2.5);

        for(int i = 0; i < numberOfPoints; i++){
            pointLocations[i][0] = times * pointLocations[i][0] + sizeX/2;
            pointLocations[i][1] = -times * pointLocations[i][1] + sizeY/2;
        }
    }

}