package fileUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import org.apache.log4j.Logger;

public class DeleteUploadFiles {
	public static Logger log=Logger.getLogger(DeleteUploadFiles.class);
 /**
  * 删除某个文件夹下的所有文件夹和文件
  *
  * @param delpath
  *            String
  * @throws FileNotFoundException
  * @throws IOException
  * @return boolean
  */
 public boolean deletefile(String delpath) throws Exception {
  try {

   File file = new File(delpath);
   // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
   if (!file.isDirectory()) {
    file.delete();
   } else if (file.isDirectory()) {
    String[] filelist = file.list();
    for (int i = 0; i < filelist.length; i++) {
     File delfile = new File(delpath + File.separator + filelist[i]);
     if (!delfile.isDirectory()) {
      delfile.delete();
     log.info(delfile.getAbsolutePath() + "删除文件成功");
     } else if (delfile.isDirectory()) {
      deletefile(delpath +  File.separator  + filelist[i]);
     }
    }
   log.info(file.getAbsolutePath()+"删除成功");
    file.delete();
   }

  } catch (FileNotFoundException e) {
  log.info("deletefile() Exception:" + e.getMessage());
  }
  return true;
 }
}