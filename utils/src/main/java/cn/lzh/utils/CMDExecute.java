package cn.lzh.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CMDExecute {

	public synchronized String run(String[] cmd, String dir)
			throws IOException {
		StringBuilder result = new StringBuilder();

		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			// set working directory
			if (dir != null)
				builder.directory(new File(dir));
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				result.append(new String(re));
			}
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}

	public static void main(String[] args) {
		if(args == null){
			args = new String[]{"notepad"};
		}
		try {
			System.out.println(new CMDExecute().run(args, null));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

