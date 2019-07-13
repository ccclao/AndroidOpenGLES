package com.yinge.opengl.util;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.InputStream;

/**
 * 功能：
 * </p>
 * <p>Copyright corp.netease.com 2018 All right reserved </p>
 *
 * @author tuke 时间 2019/7/7
 * @email tuke@corp.netease.com
 * <p>
 * 最后修改人：无
 * <p>
 */
public class OpenGlUtils {

    private static final String TAG = "ShaderUtils";


    /**
     * 根据着色器代码 文件路径 创建主程序
     * @param res 资源对象
     * @param vertexResPath   顶点着色器 文件路径
     * @param fragmentResPath 片元着色器 文件路径
     * @return
     */
    public static int createProgram(Resources res, String vertexResPath,String fragmentResPath){
        return createProgram(loadShaderSrcFromAssetFile(res, vertexResPath), loadShaderSrcFromAssetFile(res, fragmentResPath));
    }

    /**
     * 根据 着色器代码 创建 主程序
     * @param vertexSrcCode 顶点着色器 代码
     * @param fragSrcCode   片元着色器 代码
     * @return
     */
    public static int createProgram(String vertexSrcCode, String fragSrcCode){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSrcCode);
        int fragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragSrcCode);
        if (vertexShader == 0 || fragShader == 0) {
            return 0;
        }
        return createProgram(vertexShader, fragShader);

    }


    /**
     * 绑定着色器，链接主程序
     * @param vertexShader
     * @param fragShader
     * @return
     */
    public static int createProgram(int vertexShader, int fragShader) {
        if ( vertexShader == 0 || fragShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        // 绑定顶点着色器
        GLES20.glAttachShader(program, vertexShader);
        // 绑定片元着色器
        GLES20.glAttachShader(program, fragShader);
        // 链接主程序
        GLES20.glLinkProgram(program);

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_COMPILE_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            Log.e(TAG,"Could not compile program:" + program);
            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    /**
     * 加载源代码，编译shader
     * @param type    {@link GLES20#GL_VERTEX_SHADER,GLES20#GL_FRAGMENT_SHADER}
     * @param srcCode
     * @return
     */
    public static int loadShader(int type, String srcCode) {
        // 创建shader
        int shader = GLES20.glCreateShader(type);
        // 加载源代码
        GLES20.glShaderSource(shader, srcCode);
        // 编译shader
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        // 查看编译状态
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {

            Log.e(TAG,"Could not compile shader:" + shader
                    + " type = " + (type == GLES20.GL_VERTEX_SHADER ? "GL_VERTEX_SHADER" : "GL_FRAGMENT_SHADER") );
            Log.e(TAG,"GLES20 Error:"+ GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        return shader;
    }

    /**
     * 从文件中 shader 源码
     * @param resources
     * @param shaderNamePath
     * @return
     */
    public static String loadShaderSrcFromAssetFile(Resources resources, String shaderNamePath) {
        StringBuilder result=new StringBuilder();
        try{
            InputStream is=resources.getAssets().open(shaderNamePath);
            int ch;
            byte[] buffer=new byte[1024];
            while (-1!=(ch=is.read(buffer))){
                result.append(new String(buffer,0,ch));
            }
        }catch (Exception e){
            return null;
        }
//        return result.toString().replaceAll("\\r\\n","\n");
        return result.toString().replaceAll("\\r\\n","");

    }


}