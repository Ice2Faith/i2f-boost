package i2f.core.network.net.http.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/4/2 11:20
 * @desc
 */
@Data
@NoArgsConstructor
public class MultipartFile {
    private String name;
    private String fileName;
    private Long length;
    private InputStream inputStream;
    public MultipartFile(File file) throws FileNotFoundException {
        this.name="file";
        this.fileName=file.getName();
        this.length=file.length();
        this.inputStream=new FileInputStream(file);
    }
    public MultipartFile(String name,File file) throws FileNotFoundException {
        this.name=name;
        this.fileName=file.getName();
        this.length=file.length();
        this.inputStream=new FileInputStream(file);
    }
}
