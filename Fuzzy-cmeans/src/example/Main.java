package example;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import cartesian.coordinate.CCPoint;
import cartesian.coordinate.CCSystem;

public class Main extends JFrame {
  static String URL="";// Dosya yolu
  static boolean showCalculation=true;// Hesaplar konsolda gösterilsin mi?
  private static final long serialVersionUID = 1L;
  static float sizePoint=5;// Noktalarýn büyüklüðü
  static ArrayList<String> kordinat =new ArrayList<String>();
  public static ArrayList<ArrayList<Double>> new_vectorU; // Yeni oluþan vektor (algoritma bazlý)
  public static ArrayList<ArrayList<Double>> centerPoints; // Merkez noktalarý (algoritma bazlý veri)
  static int iterasyon=0;// Algoritmanýn çalýþma adýmý
  static JFrame frameKoptimal=new JFrame("Coordinate System");// Optimal k yý tespit etmek için kullanýlacak olan frame
  static CCSystem systemOptimal = new CCSystem(-0.1, -0.1, 1.0, 1.0);
  static int kntrl=0;
  static int ideaTrashOld=1;
  static double inputEpsilon=0.01;// Algoritma bazlý default epsilon deðeri
  static int countCluster=2; // Algoritma bazlý default k sýnýf sayýsý
  static int mValue=2; // Algoritma bazlý m parametresi
  static ArrayList<ArrayList<Double>> cordinates; // Alýnan kordinatlar
  static JFrame canvas=new JFrame("Coordinate System"); // Kanvas
  static CCSystem s = new CCSystem(-0.1, -0.1, 1.0, 1.0);
  static int minCluster = 2; //DBSCAN Default min cluster number
  static double maxDistance = 1;//DBSCAN Default epsilon number
  static ArrayList<ArrayList<ArrayList<Double>>> result = null;
  static JFrame OptionsCMEANS;
  // Ana algoritmanýn çalýþtýðý kýsýmdýr.
  Main(int iterasyon) throws IOException, InterruptedException {
        super("Viewer");
        // Kanvas ve önceden yüklenen veriler silindi yeniden ayarlandý.
        kordinat.clear();
        canvas.setTitle("Coordinate System");
        canvas.setVisible(true);
        canvas.setSize(800, 600);
        canvas.setLocationRelativeTo(null); 
        canvas.add(s);
        canvas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        s.clear();
        
        // Kordinatlar alýndý.
        readFile(URL,kordinat);       
        cordinates=new ArrayList<ArrayList<Double>>();
        
        // Noktalarý Canvasa Aktar
        for(int i=0;i<kordinat.size();i++){
        	ArrayList<Double> cordi=new ArrayList<Double>();
        	ArrayList<String> axis = new ArrayList<>(Arrays.asList(kordinat.get(i).split(";"))); // Eksen deðerleri (X,Y)
        	// Dosyadan okunan kordinatlar (axis[0] ve axis[1]) kanvasa aktarýldý.
        	s.add(new CCPoint(Double.parseDouble(axis.get(0)),Double.parseDouble(axis.get(1)),Color.black,sizePoint));   
        	
        	// X , Y , Fikir , Kelimeler þeklinde alýnan girdi cordi dizisine oda cordinates dizi dizisine aktarýyor.
        	cordi.add(Double.parseDouble(axis.get(0)));
        	cordi.add(Double.parseDouble(axis.get(1)));
        	cordinates.add(cordi);
        }
    
        double outputEpsilon=9999.0;
        //Baþkangýç Ýçin Bir Ayýrma Matrisi Oluþturulur.
        ArrayList<ArrayList<Double>> vectorU=new ArrayList<ArrayList<Double>>();
        int say=0;
      
        //Calculate VectorU Matrix Value And Create VectorU Matrix   
        int startPoint=0;
        for(int i=0;i<countCluster;i++){
        	 ArrayList<Double> memberValue=new ArrayList<Double>();
        	 for(int j=0;j<kordinat.size();j++){
        		 if(i!=countCluster-1 && memberValue.size()==startPoint){
        			 memberValue.add(1.0);
        			 say++;
        		 }else if(i!=countCluster-1 && memberValue.size()!=startPoint)
        			memberValue.add(0.0);
        		 
        		 if(i==countCluster-1 && kordinat.size()-say>0){
        			 memberValue.add(1.0);
        			 say++;
        		 }else if(i==countCluster-1 && kordinat.size()-say==0)
        			 memberValue.add(0.0);
        	 }
        	 if(i!=countCluster-1)
        	    vectorU.add(memberValue);
        	 else{
        		 Collections.reverse(memberValue);
        		 vectorU.add(memberValue);
        	 }
        	 startPoint++;
        }
        
        say=0;
        while(outputEpsilon>inputEpsilon && iterasyon>say){
        	//TimeUnit.SECONDS.sleep(3);
        	 s.clear();
        	drawCordi(vectorU,-1);
        	say++; 
	        //Merkez Noktalarýnýn Elde Edilmesi
	        centerPoints=new ArrayList<ArrayList<Double>>();     	         
	        for(int i=0;i<countCluster;i++){
	        	ArrayList<Double> point=new ArrayList<Double>();
	        	double v11Pay=0;
	        	double v12Pay=0;
	        	double payda=0;
		        for(int j=0;j<cordinates.size();j++){
		        	v11Pay+=Math.pow(vectorU.get(i).get(j),mValue)*cordinates.get(j).get(0);   	
		        	v12Pay+=Math.pow(vectorU.get(i).get(j),mValue)*cordinates.get(j).get(1);  
		        	payda+=Math.pow(vectorU.get(i).get(j),mValue);
		        }
		        point.add(v11Pay/payda);
		        point.add(v12Pay/payda);
		        centerPoints.add(point);
	        }
	        
	        sizePoint=25;
	        //colorClasses.clear();
	        for(int i=0;i<centerPoints.size();i++){
	        	// if(showCalculation==true)System.out.println(centerPoints.get(i)); 
	        	 //double[] x = new double[]{centerPoints.get(i).get(0)-0.05f,centerPoints.get(i).get(0),centerPoints.get(i).get(0)+0.05f};
	             //double[] y = new double[]{centerPoints.get(i).get(1)-0.05f,centerPoints.get(i).get(1)+0.05f,centerPoints.get(i).get(1)-0.05f};
	             //Color sinifColor =new Color((int)(Math.random() * 0x1000000));   
	             //CCPolygon ccp = new CCPolygon(x, y,sinifColor);	        	    	        	
	             //s.add(ccp);
	             //colorClasses.add(sinifColor);
	        	s.add(new CCPoint(centerPoints.get(i).get(0),centerPoints.get(i).get(1),colorClasses.get(i),sizePoint));
	        }
	        sizePoint=5;        
	        //Mesafelerin elde edilmesi	        
	        ArrayList<ArrayList<Double>> disEuclidien=new ArrayList<ArrayList<Double>>(); 	        
	        for(int i=0;i<countCluster;i++){
	        	ArrayList<Double> distance=new ArrayList<Double>();       	
		        for(int j=0;j<cordinates.size();j++){
		        	double disX=0;
		        	double disY=0;
		        	disX=Math.pow((cordinates.get(j).get(0)-centerPoints.get(i).get(0)),2);
		        	disY=Math.pow((cordinates.get(j).get(1)-centerPoints.get(i).get(1)),2);
		        	distance.add(Math.sqrt((disX+disY)));		        	
		        }  
	      	    disEuclidien.add(distance);       
	        }
	       // Yeni Bir Ayýrma Matrisi Oluþturulur.
	       new_vectorU=new ArrayList<ArrayList<Double>>();
	       for(int i=0;i<countCluster;i++){
	        	 ArrayList<Double> memberValue=new ArrayList<Double>();      	 
	        	 for(int j=0;j<disEuclidien.get(i).size();j++){
	        		 double value=0; 
	        		 for(int k=0;k<disEuclidien.size();k++){	       			 
	        			 value+=Math.pow(disEuclidien.get(i).get(j)/disEuclidien.get(k).get(j),mValue==1?0:(2/(mValue-1)));	    	
	            	 }
	        		 if (Double.isInfinite(value))
	        			 memberValue.add(0.0);
	        		 else if (Double.isNaN(value))
	        			 memberValue.add(1.0);
	    			 else
	    				 memberValue.add(1/value);        		 
	        	 }  	
	        	 new_vectorU.add(memberValue);
	        }
	        //Epsilon hesaplama
	        ArrayList<ArrayList<Double>> dis_vectorU=new ArrayList<ArrayList<Double>>();
	        for(int i=0;i<vectorU.size();i++){
	        	ArrayList<Double> memberValue=new ArrayList<Double>();
	        	for(int j=0;j<vectorU.get(i).size();j++)
	        		memberValue.add(vectorU.get(i).get(j)-new_vectorU.get(i).get(j)); 	
	        	dis_vectorU.add(memberValue);
	        }
	        ArrayList<Double> maxValues=new ArrayList<Double>();
	        for(int i=0;i<dis_vectorU.size();i++)
	        	maxValues.add(Collections.max(dis_vectorU.get(i)));
	        
	        outputEpsilon=Collections.max(maxValues);      	        
	        vectorU=new_vectorU;
        }   
    }  
  
  // Arayüz ekraný
  public static void main(String[] args) throws IOException, InterruptedException {
    	for(int i=0;i<3;i++)
    		colorClasses.addAll(colorClasses);
        JFrame Options= new JFrame("Fuzzy Cmeans");
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        Options.setContentPane(contentPane);
        contentPane.setLayout(null);        
       
        JLabel labelURL = new JLabel("File URL :");
        labelURL.setSize(100,150);
        labelURL.setBounds(15, 15, 64, 20);
        contentPane.add(labelURL);
        
        JTextField inputUrl = new JTextField();
        inputUrl.setText("C:/"); 
        inputUrl.setEnabled(false);
        inputUrl.setBounds(70, 15, 370,20);
        contentPane.add(inputUrl);
        
        JButton openFile= new JButton("Open");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 String userDirLocation = System.getProperty("user.dir"); // default dizin
            	 JFileChooser fileChooser = new JFileChooser(userDirLocation);
            	 fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter() );
            	 fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
            	 fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));           	
            	    int returnValue = fileChooser.showOpenDialog(null);
            	    if (returnValue == JFileChooser.APPROVE_OPTION)
            	    {
            	        File selectedFile = fileChooser.getSelectedFile();           	      
            	        inputUrl.setText("..\\ "+selectedFile.getName());
            	        URL=selectedFile.getName();
            	    }
            	    //Dosyalarý içe aktar
        	    kordinat.clear();
        	    
         	   readFile(URL,kordinat);  
         	  
                cordinates=new ArrayList<ArrayList<Double>>();

                for(int i=0;i<kordinat.size();i++){
 	               	ArrayList<Double> cordi=new ArrayList<Double>();
 	               	ArrayList<String> axis = new ArrayList<>(Arrays.asList(kordinat.get(i).split(";"))); // Eksen deðerleri (X,Y)        
 	               	cordi.add(Double.parseDouble(axis.get(0)));
 	               	cordi.add(Double.parseDouble(axis.get(1)));	        
 	               	cordinates.add(cordi);
                }	  
            }
        });
        openFile.setBounds(450, 15,80, 20);
        contentPane.add(openFile); 
        
        JLabel chooseExam = new JLabel("Choose Example :");
        chooseExam.setSize(100,150);
        chooseExam.setBounds(15, 45, 120, 20);
        contentPane.add(chooseExam);
        
        JComboBox<String> fileUrls=new JComboBox<String>();         
        fileUrls.setSize(100,150);
        fileUrls.setBounds(130, 45, 150,20); 
      
        fileUrls.setToolTipText("Choose From Example Files");  
        fileUrls.addItem("DB-1.csv");   
        fileUrls.addItem("DB-2.csv"); 
        fileUrls.addItem("DB-3.csv");   
        fileUrls.addItem("DB-4.csv");   
        fileUrls.addItem("DB-5.csv");   
        fileUrls.addItem("DB-6.csv");   
        fileUrls.addItem("DB-7.csv");   
        fileUrls.addItem("DB-8.csv");  
        fileUrls.addItem("Aggegation.csv");   
        fileUrls.addItem("Spiral.csv");   
        fileUrls.addItem("Jain.csv");   
        fileUrls.addItem("Path.csv");   
        fileUrls.addItem("R15.csv");   
        fileUrls.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                URL=(String) fileUrls.getSelectedItem();
            }
        });
        fileUrls.setSelectedIndex(-1);
        contentPane.add(fileUrls);
   
        JButton optimalK= new JButton("Elbow");
        optimalK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	findOptimalKValue();
            }
        });
        optimalK.setBounds(450, 45,80, 20); 
        contentPane.add(optimalK); 
     
        JButton openSystem = new JButton("Show Points");
        openSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             if(URL!=null && URL!=""){   
            	try {           	 
			       new Main(0);
			   } catch (IOException | InterruptedException e1 ) {
				   // TODO Auto-generated catch block
				   e1.printStackTrace();
			   }
            }else
         	   JOptionPane.showMessageDialog(null, "No file selected yet.", "Information", JOptionPane.INFORMATION_MESSAGE);   
            }
        });
        openSystem.setBounds(290, 45, 150,20); 
        contentPane.add(openSystem);       
        
        JButton execCMeans = new JButton("C Means On Points)");
        execCMeans.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(URL!=null && URL!=""){
	               try {
	            	   countCluster= Integer.parseInt(JOptionPane.showInputDialog("Enter the class number.")); 
					   new Main(9999);
				   } catch (IOException | InterruptedException|NumberFormatException e1) {
					   // TODO Auto-generated catch block
					   e1.printStackTrace();
				   }
               }else
            	   JOptionPane.showMessageDialog(null, "No file selected yet.", "Information", JOptionPane.INFORMATION_MESSAGE);
             }
        });
        execCMeans.setBounds(15, 75, 265, 25);
        contentPane.add(execCMeans);   
               
        JButton before = new JButton("<- Before ");
        before.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(URL!=null && URL!=""){	
               try {        
            	   if(iterasyon!=0)
            	   iterasyon--;       
				   new Main(iterasyon);
			   } catch (IOException | InterruptedException e1) {
				   // TODO Auto-generated catch block
				   e1.printStackTrace();
			   }
            }else
               JOptionPane.showMessageDialog(null, "No file selected yet.", "Information", JOptionPane.INFORMATION_MESSAGE);    	     
            }
        });
        before.setBounds(290, 75, 115, 25);
        contentPane.add(before);   
        
        JButton next = new JButton("Next -> ");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {         	
              if(URL!=null && URL!=""){
            	try {         		
            	   iterasyon++;
				   new Main(iterasyon);
			   } catch (IOException | InterruptedException e1) {
				   // TODO Auto-generated catch block
				   e1.printStackTrace();
			   }
              }else
            	  JOptionPane.showMessageDialog(null, "No file selected yet.", "Information", JOptionPane.INFORMATION_MESSAGE);	            
            }
        });
        next.setBounds(415, 75, 115, 25);
        contentPane.add(next);       
              
        Options.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Options.setSize(565,150);
        Options.setLocation(100,100);
        Options.setVisible(true);
    }
 

  // Sýnýflara atanancak renkler 
  public static ArrayList<Color> colorClasses=new ArrayList<Color>(Arrays.asList(
   		new Color(255,0,0),
   		new Color(0,255,0),
   		new Color(255,228,19),
   		new Color(154,50,150),
   		new Color(255,165,0),
   		new Color(0,255,255),
   		new Color(139,69,19),			
   		new Color(250, 161, 254),
   		new Color(60,250,23),
   		new Color(150,10,80),
 		new Color(255, 231, 186),
   		new Color(170, 58, 140),
   		new Color(100, 150, 42),
   		new Color(127, 255, 0),
   		new Color(5, 218, 142),
   		new Color(0,255,255),
   		new Color(255, 105, 180),
   		new Color(127,255, 212),
   		new Color(0,200,255),
   		new Color(255, 127, 36),
   		new Color(153, 50, 60),
   		new Color(255,120,50),
   		new Color(16, 161, 254),
   		new Color(255,0,0),
   		new Color(0, 0, 139),		
   		new Color(255,255,0),
   		new Color(120,80,147),
   		new Color(0,255,0),	
   		new Color(28, 134, 238)	
   )); 
  
  // Kordinatlarýn kanvasa aktarýlmasý tekil iþaretleme
  public static void drawCordi( ArrayList<ArrayList<Double>> vectorU,int mark){
	    s.clear();
	     for(int i=0;i<kordinat.size();i++){	    	  
	    	  ArrayList<String> axis = new ArrayList<>(Arrays.asList(kordinat.get(i).split(";")));
	    	  int red=0;
	    	  int blue=0;
	    	  int green=0;  
	    	  if(i==mark){
	    		   red=0;
	        	   green=0; 
	        	   blue=250;
	        	   sizePoint=15;
	    	  }else{	   
   		   		 for(int j=0;j<vectorU.size();j++){		   		     
   		   	   		if( red+(int) (vectorU.get(j).get(i)*colorClasses.get(j).getRed())<=255 && green+(int) (vectorU.get(j).get(i)*colorClasses.get(j).getGreen())<=256 &&  blue+(int) (vectorU.get(j).get(i)*colorClasses.get(j).getBlue())<256){
	    		   	   	    red+=(int) (vectorU.get(j).get(i)*colorClasses.get(j).getRed());
	   	    	            green+=(int) (vectorU.get(j).get(i)*colorClasses.get(j).getGreen()); 
	   	    	            blue+=(int) (vectorU.get(j).get(i)*colorClasses.get(j).getBlue()); 
   		   	   		}
   		   		 }	    		   			    		     	     
	    	      sizePoint=5;
	    	  }
	    	  //System.out.println("red: "+red+" green:"+green+" blue:"+blue );
	          Color color=new Color(red,green,blue,255);
	         
	       	  s.add(new CCPoint(Double.parseDouble(axis.get(0)),Double.parseDouble(axis.get(1)),color,sizePoint));
	     }
  }
  
  // Kordinatlarýn kanvasa aktarýlmasý dizi iþaretleme
  public static void drawCordi( ArrayList<ArrayList<Double>> vectorU, ArrayList<Integer> mark){
	    s.clear(); 
	     for(int i=0;i<kordinat.size();i++){	    	  
	    	  ArrayList<String> axis = new ArrayList<>(Arrays.asList(kordinat.get(i).split(";")));
	    	  int red=0;
	    	  int blue=0;
	    	  int green=0;  
	    	  if(mark.contains(i)){
	    		   red=0;
	        	   green=0; 
	        	   blue=250;
	        	   sizePoint=15;
	    	  }else{
	    		  for(int j=0;j<vectorU.size();j++){		   		     
 		   	   		if( red+(int) (vectorU.get(j).get(i)*colorClasses.get(j).getRed())<=255 && green+(int) (vectorU.get(j).get(i)*colorClasses.get(j).getGreen())<=256 &&  blue+(int) (vectorU.get(j).get(i)*colorClasses.get(j).getBlue())<256){
	    		   	   	    red+=(int) (vectorU.get(j).get(i)*colorClasses.get(j).getRed());
	   	    	            green+=(int) (vectorU.get(j).get(i)*colorClasses.get(j).getGreen()); 
	   	    	            blue+=(int) (vectorU.get(j).get(i)*colorClasses.get(j).getBlue()); 
 		   	   		}
 		   		 }	
	    	      sizePoint=5;
	    	  }
	    	  //System.out.println("red: "+red+" green:"+green+" blue:"+blue );
	          Color color=new Color(red,green,blue,255);
	       	  s.add(new CCPoint(Double.parseDouble(axis.get(0)),Double.parseDouble(axis.get(1)),color,sizePoint));
	     }
  }
  
  // Merkez noktalarýn kanvasa aktarýmý
  public static void drawCenter(){
   	 sizePoint=25;
	     for(int i=0;i<centerPoints.size();i++)
	       s.add(new CCPoint(centerPoints.get(i).get(0),centerPoints.get(i).get(1),colorClasses.get(i),sizePoint));        
	     sizePoint=5;   	
  }
     
  // C means noktalarý ayýklama
  private static ArrayList<Integer> findClassPoint(ArrayList<ArrayList<Double>> new_vectorU, int selectedIndex) {
    	ArrayList<Integer> ownerClass=new ArrayList<Integer>();   	
    	int tempindis=0;
    	for(int i=0;i<new_vectorU.get(0).size();i++){
    		double max = 0;
	   		 for(int j=0;j<new_vectorU.size();j++)
	   	   		if(max<new_vectorU.get(j).get(i)){
	   	   			max=new_vectorU.get(j).get(i);
	   	   			tempindis=j;
	   	   		} 	   	   	  	   	   	 
	   		if(tempindis==selectedIndex)
	   			ownerClass.add(i);   				   		
	   	 }  	
    	return ownerClass;
	}
   
  // Elbow tekniðiyle optimum k deðerinin tespiti
  public static void findOptimalKValue(){
    	 if(URL!=null && URL!=""){
	        frameKoptimal.setTitle("Coordinate System");
	        frameKoptimal.setVisible(true);
	        frameKoptimal.setSize(800, 600);
	        frameKoptimal.setLocationRelativeTo(null); 
	        frameKoptimal.add(systemOptimal);
	        frameKoptimal.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        systemOptimal.clear();
	        kordinat.clear();
	        readFile(URL,kordinat);   
	        
	        try {
	        	System.out.println(kordinat.size());
	        	double tempsse=99;
	        	double sse=1;
	        	int i=0;
	        	while(Math.abs(tempsse-sse)>0.1 && i<100){
	        		i++;
	        		tempsse=sse;
	        		countCluster=i;
	        		new Main(999);
	        		canvas.dispose();
	        		sse=0;
	        		for(int j=0;j<new_vectorU.get(0).size();j++){
	        			double max=0.0;
	        			int tempk = 0;
	        			for(int k=0;k<new_vectorU.size();k++){
	        				if(max<new_vectorU.get(k).get(j)){
	        					max=new_vectorU.get(k).get(j);
	        					tempk=k;
	        				}
	        			}
	        			
	        			sse += Math.pow((cordinates.get(j).get(0)-centerPoints.get(tempk).get(0))+(cordinates.get(j).get(1)-centerPoints.get(tempk).get(1)),2);
	        		}
	        		systemOptimal.add(new CCPoint(i,sse,sizePoint));
	        		System.out.println("k: "+i+" tempsse: "+tempsse+" sse: "+sse);
	        		
	        	}    
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
    	 }else
    		 JOptionPane.showMessageDialog(null, "No file selected yet.", "Information", JOptionPane.INFORMATION_MESSAGE);   	 
   }

  // Dosya okuma iþlemi -*.csv
  public static void readFile(String URL,ArrayList<String> sinif){
	    File file = new File(URL);	
	    try {
	        BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-9"));
	        String text;
	        while ((text = fis.readLine()) != null) {
	        	if(text.indexOf(',')!=-1)text=text.replace(',','.');
	            sinif.add(text.toLowerCase());
	        }          
	        fis.close(); // dosya okuma iþlemimiz bittikten sonra kapatÄ±yoruz.
	    } catch (FileNotFoundException e) {
	        // TODO: handle exception
	       // System.out.println("Dosya BulunamadÄ±..");
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}    
   }