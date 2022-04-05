package com.hjc;

import java.sql.*;
import java.io.*;

public class CW {
    private static  String JDBC_DRIVER	= "org.sqlite.JDBC";
    private static  String DATABASE_LOCATION = "jdbc:sqlite:";

    public static Connection con = null;
    public static Statement stat = null;

    public String dbName = null; // Database name
    public String fileName = null; // The name and format of the storage file
    public String copyName = null; // Database name after copying
    public String jdbcHead = "jdbc:sqlite:";



    FileWrite fw = new FileWrite();

    public void noti( String message, Exception e ) {
        System.out.println( message + " : " + e );
        e.printStackTrace ( ); //Catch exception
        System.exit( 0 ); //Normal exit
    }

    //connect to database
    private void getCon( ) throws SQLException {
        try {
            con = DriverManager.getConnection(DATABASE_LOCATION + dbName);
            con.setAutoCommit(false); //Maintain data integrity
        }
        catch ( SQLException sqle ) {
            noti( "Db.getConnection database location [" + DATABASE_LOCATION + "] db name[" + dbName + "]", sqle);
            con.commit( ); // submit update
            con.close ( );
        }
    }

    //JDBC operation
    private void open( ) {
        File dbf = new File(dbName);
        //检验
        if ( dbf.exists( ) == false ) {
            System.out.println("SQLite database file [" + dbName + "] does not exist");
            System.exit( 0 );
        }
        try {
            Class.forName( JDBC_DRIVER );
            getCon( );
        }
        catch (ClassNotFoundException | SQLException cnfe ) {
            noti( "Db.Open", cnfe );
        }
        System.out.println("Opened database successfully"); //connection successful
    }

    //variable assignment
    public CW( String _dbName, String _fileName ,String _copyName) {
        dbName = _dbName;
        fileName = _fileName;
        copyName = _copyName;

        System.out.println("Opening "+ _dbName);
        open( );
    }

    public void getData()
    {
        try{
            ResultSet result = null;
            DatabaseMetaData database = con.getMetaData(); // connect to metadata
            result = database.getTables(null,null,null,new String[]{"TABLE"}); //get table

            String[] Colname = new String[20]; //column
            String[] CN = new String[20];
            String[] datatype = new String[20];

            DatabaseMetaData database2 = con.getMetaData();

            String[] Colname2 = new String[20];
            String[] datatype2 = new String[20];

            DatabaseMetaData database3 = con.getMetaData();

            String[] indexName = new String[20];
            String[] CN3 = new String[20];
            String[] ascType = new String[20];

            while(result.next())
            {
                int i=0;

                boolean firstFK = true; //跟踪是否是列出的第一个外键
                boolean pkPresent = true; //跟踪之前是否有主键
                int x=0;
                String table_name2 = result.getString("TABLE_NAME");
                System.out.println(table_name2); //输出表名

                System.out.print("\n");
                System.out.print("DROP TABLE if EXISTS \""+table_name2+"\";\n");
                String s_0 = "DROP TABLE if EXISTS \""+table_name2+"\";\n";
                FileWrite.fileWrite(fileName, s_0);

                System.out.print("CREATE TABLE IF NOT EXISTS \""+table_name2+"\"(");
                String s_1 = "CREATE TABLE IF NOT EXISTS \""+table_name2+"\"(";
                FileWrite.fileWrite(fileName, s_1);

                ResultSet col4 = database2.getColumns(null,null,table_name2,null);
                while(col4.next()) {
                    Colname2[x] = col4.getString("COLUMN_NAME");
                    datatype2[x] = col4.getString("TYPE_NAME");

                    System.out.print(" \""+ Colname2[x] + "\" "+datatype2[x] + ",");
                    String s_2 = " \"" + Colname2[x] + "\" " + datatype2[x] + ",";
                    FileWrite.fileWrite(fileName, s_2);
                    x++;
                }

//获取主键
                ResultSet PK2 = database2.getPrimaryKeys(null,null, table_name2);
                System.out.print(" PRIMARY KEY (");
                String s_3 = " PRIMARY KEY (";
                FileWrite.fileWrite(fileName, s_3);

                while(PK2.next()) {
                    if( firstFK==false){
                        System.out.print(", ");
                        String s_4 = ", ";
                        FileWrite.fileWrite(fileName, s_4);
                    }
                    System.out.print( PK2.getString("COLUMN_NAME"));
                    String s_5 = PK2.getString("COLUMN_NAME");
                    FileWrite.fileWrite(fileName, s_5);

                    if( firstFK==true){
                        firstFK = false;
                    }
                }
                System.out.print(")");
                String s_5_5 = ")";
                FileWrite.fileWrite(fileName, s_5_5);
//获取外键
                ResultSet FK = database2.getImportedKeys(null, null, table_name2);
                while(FK.next()) {
                    if(  pkPresent ==true){
                        System.out.print(",");
                        String s_6 = ",";
                        FileWrite.fileWrite(fileName, s_6);
                    }

                    if( pkPresent==false){
                        System.out.print(", ");
                        String s_7 = ", ";
                        FileWrite.fileWrite(fileName, s_7);
                    }

                    System.out.print(" FOREIGN KEY (\""+ FK.getString("FKCOLUMN_NAME") +"\")"+ " REFERENCES " + FK.getString("PKTABLE_NAME") + "(\"" + FK.getString("PKCOLUMN_NAME")+"\")");
                    String s_8 = " FOREIGN KEY (\""+ FK.getString("FKCOLUMN_NAME") +"\")"+ " REFERENCES " + FK.getString("PKTABLE_NAME") + "(\"" + FK.getString("PKCOLUMN_NAME")+"\")";
                    FileWrite.fileWrite(fileName, s_8);

                    if(  pkPresent ==true){
                        pkPresent  = false;
                    }
                }

                System.out.print(");");
                String s_10 = ");";
                FileWrite.fileWrite(fileName, s_10);

                System.out.println("\n");
                String s_11 = "\n";
                FileWrite.fileWrite(fileName, s_11);

                String table_name = result.getString("TABLE_NAME");//获取数据库中表的名称


                ResultSet col = database.getColumns(null,null,table_name,null);           // gets columns from the table
                while(col.next())
                {
                    Colname[i] = col.getString("COLUMN_NAME");
                    datatype[i] = col.getString("DATA_TYPE");
                    i++;
                }
                Statement q2 =  con.createStatement( );
                ResultSet rSet2 = q2.executeQuery("SELECT * FROM "+ table_name +";");
                while(rSet2.next()){
                    for(int j=0;j<20;j++){
                        try{
                            CN[j] = rSet2.getString(j+1);
                        }
                        catch(SQLException e) {
                            break;
                        }
                    }

                    System.out.print("INSERT INTO \""+table_name+"\" VALUES(");
                    String s_12 = "INSERT INTO \""+table_name+"\" VALUES(";
                    FileWrite.fileWrite(fileName, s_12);

                    for(int z =0;z<20 ;z++)
                    {
                        if(CN[z]!=null){

                            if(datatype[z].equals("12")){
                                System.out.print("\""+CN[z]);
                                String s_13 = "\""+CN[z];
                                FileWrite.fileWrite(fileName, s_13);

                            }
                            else{
                                System.out.print("\"" + CN[z]);
                                String s_14 = "\"" + CN[z];
                                FileWrite.fileWrite(fileName, s_14);

                            }
                            CN[z]=null;
                        }
                        else {
                            break;
                        }

                        if(CN[z+1]!=null){
                            System.out.print("\",");
                            String s_15 = "\",";
                            FileWrite.fileWrite(fileName, s_15);
                        }
                        else {
                            break;
                        }
                    }
                    System.out.print("\");");
                    String s_16 = "\");";
                    FileWrite.fileWrite(fileName, s_16);

                    System.out.println("\n");
                    String s_17 = "\n";
                    FileWrite.fileWrite(fileName, s_17);
                }
                rSet2.close();
                q2.close();

                x=0;
                String table_name3 = result.getString("TABLE_NAME");

                ResultSet col2 = database3.getIndexInfo(null,null,table_name3,true, false);
                while(col2.next()) {
                    System.out.print("CREATE INDEX ");

                    indexName[x] = col2.getString("INDEX_NAME");
                    CN3[x] = col2.getString("COLUMN_NAME");
                    ascType[x] = col2.getString("ASC_OR_DESC");
                    if(ascType[x]==null){
                        System.out.print(indexName[x]+" ON "+table_name3+ " ("+CN3[x]);
                        x++;
                    }else{
                        System.out.print(indexName[x]+" ON "+table_name3+ " ("+CN3[x] + " "+ascType[x]);
                        x++;
                    }
                    System.out.print(");");
                    System.out.println("\n");
                }
                System.out.println("\n");
                String s_18 = "\n";
                FileWrite.fileWrite(fileName, s_18);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void copyDB() throws IOException, SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection(jdbcHead + copyName);
        stat = con.createStatement();
        //执行脚本
        stat.executeUpdate(readFile());

        System.out.println("Database copied!");
    }




    //读取脚本内容
    public String readFile(){
        File file = new File(fileName);
        StringBuilder result = new StringBuilder(); //构建对象，一个字符序列可变的字符串，方便操作

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String s = null;
            //使用readLine方法，一次读一行
            while((s = br.readLine())!=null){
                result.append( System.lineSeparator() + s);// 后面加，直接修改result的值
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}
