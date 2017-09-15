/**
 * 封装sqoop export操作的一系列方法
 */
package usi.bdma.client.sqoop;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.cloudera.sqoop.*;
import com.cloudera.sqoop.tool.ExportTool;
import com.cloudera.sqoop.tool.SqoopTool;

/**
 * @author Liu
 *
 */
public class SqoopExport  extends Configured implements Tool {
	 private static final String connectionString = "jdbc:oracle:thin:@192.168.223.54:1521:XE";
	  private static final String username = "SYSTEM";
	  private static final String password = "LJL1991";
	  /*
	   * 将数据库连接使用的路径、用户名以及密码传入
	   */
	  public static void setUp() {
		  }
	  /*
	   * 将表导出到数据库中
	   * @parm tableName:表名
	   * @parm hdfsPath:hdsf的路径
	   */
	  @SuppressWarnings("deprecation")
	  public  void exportToHdfs(String tableName,String hdfsPath) throws Exception {
		SqoopOptions SqoopOptions = new SqoopOptions(getConf());
		    SqoopOptions.setConnectString(connectionString);
		    SqoopOptions.setUsername(username);
		    SqoopOptions.setPassword(password);
		   SqoopOptions.setTableName(tableName);
		   SqoopOptions.setExportDir(hdfsPath);
		   SqoopOptions.setNumMappers(1);//map数量默认设值为1
		   SqoopOptions.setInNullNonStringValue("");// 	可选参数，如果没有指定，则字符串null将被使用
		   SqoopOptions.setInNullStringValue("");// 	可选参数，如果没有指定，则字符串null将被使用
		   SqoopOptions.setFieldsTerminatedBy(',');// 字段之间的分隔符
		  SqoopTool exportTool = new ExportTool();
		    Sqoop sqoop = new Sqoop(exportTool, getConf(), SqoopOptions);
		    int ret = Sqoop.runSqoop(sqoop, new String[0]);
		    if (0 != ret) {
		      throw new Exception("Error doing export; ret=" + ret);
		    }
	  }
	  
	  public int run(String [] args) {
		    String tableName = "SQOOP_TEST1";
		    String hdfsPath = "/user/sqoop2";

		    try {
	    	  exportToHdfs(tableName,hdfsPath);
		    } catch (Exception e) {
//		      System.err.println("Error: " + StringUtils.stringifyException(e));
		    	System.out.println(e.getMessage());
		      return 1;
		    }

		    return 0;
		  }

		  public static void main(String [] args) throws Exception {
		    SqoopExport test = new SqoopExport();
		    int ret = ToolRunner.run(test, args);
		    System.exit(ret);
		  }
}