package cn.lzh.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CMDExecute {

	public synchronized String run(String[] cmd, String dir)
			throws IOException {
		String result = "";

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
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		String result = null;
		CMDExecute cmdExecute = new CMDExecute();
		try {
			result = cmdExecute.run(args, "D:\\MyProject\\colimas\\axis_c");
			System.out.println(result);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

