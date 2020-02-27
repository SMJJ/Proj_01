package com.liaomj.mr;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.liaomj.util.LogParser;

public class MapRdDriver {
	static class MyMapper extends Mapper<LongWritable, Text, NullWritable, Text>{
		private LogParser parser;
		
		@Override
        protected void setup(Context context) throws IOException, InterruptedException {
            parser = new LogParser();
        }
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String log = value.toString();
			Map<String,String> logInfo = parser.parse(log);
			// ip,url,sessionId,time,country,province,city,pageId
			String ip = logInfo.get("ip");
			String url = logInfo.get("url");
			String sessionId = logInfo.get("seesionId");
			String time = logInfo.get("time");
			String country = logInfo.get("country") == null ? "-" : logInfo.get("country");
			String province = logInfo.get("province") == null ? "-" : logInfo.get("province");;
			String city = logInfo.get("city") == null ? "-" : logInfo.get("city");
			String pageId = logInfo.get("pageId") == null ? "-" : logInfo.get("pageId");
			
            StringBuilder builder = new StringBuilder();
            builder.append(ip).append("\t");
            builder.append(url).append("\t");
            builder.append(sessionId).append("\t");
            builder.append(time).append("\t");
            builder.append(country).append("\t");
            builder.append(province).append("\t");
            builder.append(city).append("\t");
            builder.append(pageId);
            
            context.write(NullWritable.get(), new Text(builder.toString()));
		}
	}
	
	public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();

        // 濡傛灉杈撳嚭鐩綍宸茬粡瀛樺湪锛屽垯鍏堝垹闄�
        FileSystem fileSystem = FileSystem.get(configuration);
        Path outputPath = new Path("G:\\hadoop_mr\\MapReducer\\src\\output");
        if(fileSystem.exists(outputPath)) {
            fileSystem.delete(outputPath,true);
        }

        Job job = Job.getInstance(configuration);
        job.setJarByClass(MapRdDriver.class);

        job.setMapperClass(MyMapper.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);
		
		FileInputFormat.setInputPaths(job, new Path("G:\\hadoop_mr\\MapReducer\\src\\input\\trackinfo_20130721.data"));
		FileOutputFormat.setOutputPath(job, new Path("G:\\hadoop_mr\\MapReducer\\src\\output"));
		
		job.waitForCompletion(true);
	}
	
}
