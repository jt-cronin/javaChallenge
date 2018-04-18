import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.io.FileNotFoundException;

public class javaChallenge {

	public static void main(String[] args) {
		getCSVFile();
		exportDatabase();
	}
	
	
	public static void getCSVFile() {
		//The csvFile should be replaced with the location of the file
        String csvFile = "/Users/johntcronin/Desktop/JavaProgramChallenge/emp.csv";
        BufferedReader br = null;
        String cvsSplitBy = ",";

        try {
        		br = new BufferedReader(new FileReader(csvFile));
        		br.readLine();
        		String line = null;
            while ((line = br.readLine()) != null) {
            		
                String[] employees = line.split(cvsSplitBy);
                for (int x = 0; x< employees.length; x++) {
                		if (employees[x].length() == 0) {
                			employees[x] = "0";
                		}
                }
                	int empno = Integer.parseInt(employees[0]);
                String fname = employees[1];             	
            		String lname = employees[2];         		
            		int deptno = Integer.parseInt(employees[3]);
            		int mgrno = Integer.parseInt(employees[4]);
            		String salary = employees[5];
            		String commission = employees[6];
            		String template = "INSERT INTO employee"
            				+ " (empno, fname, lname, deptno, mgrno, salary, commission)"
            				+ " values (%s, '%s', '%s', %s, %s, '%s', '%s')";
            		String sql = String.format(template, empno, fname, lname, deptno, mgrno, salary, commission);
                
                System.out.println(sql);
                executeQuery(sql);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  
        } catch(Exception e){ 
        		System.out.println(e);
        	}
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	
        public static void executeQuery(String qry) {
        	try {
        		Class.forName("com.mysql.jdbc.Driver");    
        		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaChallenge","root","password"); 
        		Statement stmt = con.createStatement();  
        		stmt.executeUpdate(qry);
        		System.out.println("Inserted");
        		stmt.close();
        		}
        		catch(Exception e){ 
        			System.out.println(e);
        			}  
        	}
        	
        
        public static void exportDatabase() {
        	try {
        		Class.forName("com.mysql.jdbc.Driver");    
        		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaChallenge","root","password"); 
        		Statement stmt = con.createStatement();
        		ResultSet rs = stmt.executeQuery("SELECT * FROM employee");
        		
        		String template = "";
        		String tempStmt = "";
        		String insertStmt = "";
        		while(rs.next()) {
        			template = "%s,%s,%s,%s,%s,%s,%s\n";
        			int empno = rs.getInt("empno");
        			String fname = rs.getString("fname");            	
            		String lname = rs.getString("lname");         		
            		int deptno = rs.getInt("deptno");
            		int mgrno = rs.getInt("mgrno");
            		String salary = rs.getString("salary");
            		String commission = rs.getString("commission");
            		tempStmt = String.format(template, empno, fname, lname, deptno, mgrno, salary, commission);
            		insertStmt += tempStmt;
            		//System.out.println(insertStmt);
            		
        		}
        		System.out.println(insertStmt);
        		insertIntoCSV(insertStmt);
        		stmt.close();
        	}
        	catch(Exception e) {
        		System.out.println(e);
        	}
        }
        
        public static void insertIntoCSV(String insertStmt) {
        	try {
        		FileWriter writer = new FileWriter("emp2.csv");
        		BufferedWriter bwr = new BufferedWriter(writer);
            bwr.write(insertStmt);
            bwr.write("\n");
            bwr.close();
            System.out.println("Successfully Written to the file");
        		
        	}
        	catch(Exception e) {
        		System.out.println(e);
        	}
        	
        }
}