package com;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

public class NgramMapper extends Mapper<NullWritable, BytesWritable, Text, Text>{

//  	private final static IntWritable one = new IntWritable(1);
//	private String currentJob = "Bigram";
	private String currentJob = "Unigram";
//	private String BySyring = "year";
	private String BySyring = "lastName";
  	private Text mapkey = new Text();
  	private Text valueTuple = new Text();
	private  String previous="PreviousGram",current="currentGram";
  	
  	public void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {	
  		  String valueString = new String(value.copyBytes(),"UTF-8");
  		   
  		  switch(currentJob){
  		  		
  		  		case "Unigram":
  		  			UnigramMapParser(valueString,context,mapkey,valueTuple);
  		  			break;
  		  		case "Bigram":
  		  			BigramMapParser(valueString,context,mapkey, valueTuple);
  		  			break;
  		  }
  	}

	/**
	 * Unigram Map Parser
	 * */
	private void UnigramMapParser(String str,Context context,Text mapkey, Text valueTuple) throws IOException, InterruptedException{
		  String fileTitle = "fileTitle";
		  String lastName="lastName";
		  String year="1992";
		  int i=0;
		  String mapListUnit[]=new String[4]; 

		  String lines[] = str.split("\\r?\\n");
		  
		  //go throught the header part and get year, fileName, and 
		  for(;i<lines.length;i++){
			  String currentLine = lines[i];
			  
			  if(currentLine.length()>5&&currentLine.substring(0, 5).equals("Title")){
				  fileTitle = currentLine.substring(7, currentLine.length());
			  }
			  if(currentLine.length()>12&&currentLine.substring(0,12).equals("Release Date")){
				  // extract release data
				  //
				  String[] subs = currentLine.split("\\s+");
				  if (subs.length>2) {
					  if(subs.length>5){
						  if (subs[4].indexOf("[")>=0){
							  year = subs[3];
						  }else{
							  year = subs[4];
						  }
					  }else{
						  if (subs[subs.length-1].indexOf("]")>=0) {
							  if(subs.length>=4)
							  {  year = subs[subs.length-3];
							  }else{
								  year = "0";
							  }
						  }else{
							  year = subs[subs.length-1];
						  }
					  }
				  }else{
					  year = "0";
				  }
				  
				  
			  }
			  if (currentLine.length()>6&&currentLine.substring(0,7).equals("Author:")) {
				  String[] subs = currentLine.split("\\s+");

				  if (subs.length>1) {
					if(subs[subs.length-1].indexOf(")")>=0){
						int tempIterator=subs.length-1;
						while(tempIterator>=1&&subs[tempIterator].indexOf("(")<0){
							tempIterator--;
						}
						if (tempIterator>=2&&subs[tempIterator-1].equalsIgnoreCase("Jr.")) {
							lastName = subs[tempIterator-1];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}else{
							lastName = subs[tempIterator-1];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}
					}else{
						if (subs.length>2&&subs[subs.length-1].equalsIgnoreCase("Jr.")) {
							lastName = subs[subs.length-2];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}else{
							lastName = subs[subs.length-1];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}
					}
				  }else{
					  lastName = "";
				  }
			  }
			  
			  if(currentLine.length()>3&&currentLine.substring(0,3).equals("***")){
				  break;
			  }
		  }
		  mapListUnit[0]="";
		  mapListUnit[1]=year;
		  mapListUnit[2]=fileTitle;
		  mapListUnit[3]=lastName;

		  //start scanning the body
		  i++;

		  for (; i < lines.length; i++) {
			  String currentLine = lines[i];
			  
			  if(currentLine.length()<1){
				  continue;
			  }else{
				  currentLine=currentLine.replaceAll("[^a-zA-Z ]", "").toLowerCase();
				  String subStrs[];
				  if(currentLine.contains(" ")){
					  //current line contains multiple words
					  String subStrs2[] = currentLine.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
					  subStrs=subStrs2;
				  }else{
					  //current line only contain s single word
					  String subStrs2[] = {currentLine};
					  subStrs=subStrs2;
				  }
				  
				  for (int j = 0; j < subStrs.length; j++) {				  
					  String currentToken = subStrs[j];
					  
					  if(currentToken.equals("")){
						  continue;
					  }
					  mapListUnit[0] = currentToken;
					  
					  if (BySyring.equals("year")) {
						  mapkey.set(mapListUnit[0]+"\t"+mapListUnit[1]);
						  valueTuple.set(mapListUnit[2]+","+mapListUnit[3]);
					  }else if(BySyring.equals("lastName")){
						  mapkey.set(mapListUnit[0]+"\t"+mapListUnit[3]);
						  valueTuple.set(mapListUnit[2]+","+mapListUnit[1]);
					  }
					  
					  context.write(mapkey, valueTuple);
				  }
			  }
		  }
	  }
	/**
	 * Bigram Map Parser
	 * */
	private void BigramMapParser(String str,Context context,Text mapkey, Text valueTuple) throws IOException, InterruptedException{
		  String fileTitle = "asd";
		  String lastName="asd";
		  String year="1992";
		  int i=0;
		  String mapListUnit[]=new String[4]; 

		  String lines[] = str.split("\\r?\\n");
		  
		  //go throught the header part and get year, fileName, and 
		  for(;i<lines.length;i++){
			  String currentLine = lines[i];
			  
			  if(currentLine.length()>5&&currentLine.substring(0, 5).equals("Title")){
				  fileTitle = currentLine.substring(7, currentLine.length());
			  }
			  if(currentLine.length()>12&&currentLine.substring(0,12).equals("Release Date")){				  
				  // extract release data
				  //
				  String[] subs = currentLine.split("\\s+");
				  if (subs.length>2) {
					  if(subs.length>5){
						  if (subs[4].indexOf("[")>=0){
							  year = subs[3];
						  }else{
							  year = subs[4];
						  }
					  }else{
						  if (subs[subs.length-1].indexOf("]")>=0) {
							  if(subs.length>=4)
							  {  year = subs[subs.length-3];
							  }else{
								  year = "0";
							  }
						  }else{
							  year = subs[subs.length-1];
						  }
					  }
				  }else{
					  year = "0";
				  }
			  }
			  if (currentLine.length()>6&&currentLine.substring(0,7).equals("Author:")) {				  
				  String[] subs = currentLine.split("\\s+");

				  if (subs.length>1) {
					if(subs[subs.length-1].indexOf(")")>=0){
						int tempIterator=subs.length-1;
						while(tempIterator>=1&&subs[tempIterator].indexOf("(")<0){
							tempIterator--;
						}
						if (tempIterator>=2&&subs[tempIterator-1].equalsIgnoreCase("Jr.")) {
							lastName = subs[tempIterator-1];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}else{
							lastName = subs[tempIterator-1];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}
					}else{
						if (subs.length>2&&subs[subs.length-1].equalsIgnoreCase("Jr.")) {
							lastName = subs[subs.length-2];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}else{
							lastName = subs[subs.length-1];
							lastName = lastName.replaceAll("[^a-zA-Z ]", "");
						}
					}
				  }else{
					  lastName = "";
				  }
				  
			  }
			  
			  if(currentLine.length()>3&&currentLine.substring(0,3).equals("***")){
				  break;
			  }
		  }
		  mapListUnit[0]="";
		  mapListUnit[1]=year;
		  mapListUnit[2]=fileTitle;
		  mapListUnit[3]=lastName;
		  
//		  String[] bigSubStrings = str.split("\\*\\*\\*");
//		  String bodyString = bigSubStrings[2];
//		  
//		  bodyString = bodyString.replaceAll("\\p{P}", "").toLowerCase().replaceAll("\\p{Z}",",");
//		  
//		  String words[] = bodyString.split(",");
//		  
//		  if (BySyring.equals("year")) {
//			  mapkey.set(words[0]+"\t"+mapListUnit[1]);
//			  valueTuple.set(mapListUnit[2]+","+mapListUnit[3]);
//		  }else if(BySyring.equals("lastName")){
//			  mapkey.set(words[0]+"\t"+mapListUnit[3]);
//			  valueTuple.set(mapListUnit[2]+","+mapListUnit[1]);
//		  }
//		  context.write(mapkey, valueTuple);// write first Bigram (BLANK + UNIGRAM)
//
//		  String current ="";
//		  String previous="";
//		  current=words[0];
//		  for(int j=1;j<words.length;j++){
//			  	previous = current;
//			  	current = words[j];
//			  	
//			  	mapListUnit[0] = previous+" "+current;
//			  	
//				if (BySyring.equals("year")) {
//				  mapkey.set(mapListUnit[0]+"\t"+mapListUnit[1]);
//				  valueTuple.set(mapListUnit[2]+","+mapListUnit[3]);
//				}else if(BySyring.equals("lastName")){
//				  mapkey.set(mapListUnit[0]+"\t"+mapListUnit[3]);
//				  valueTuple.set(mapListUnit[2]+","+mapListUnit[1]);
//				}
//				
//				context.write(mapkey, valueTuple);
//		  }
		  //start scanning the body
		  i++;
		  boolean firstGram=true;
		  for (; i < lines.length; i++) {
			  String currentLine = lines[i];
			  
			  currentLine = currentLine.replaceAll("[?!.]", "endindicator");
			  
			  if(currentLine.length()<1){
				  continue;
			  }else{
				  currentLine=currentLine.replaceAll("[^a-zA-Z ]", "").toLowerCase();
				  String subStrs[];
				  if(currentLine.contains(" ")){
					  String subStrs2[] = currentLine.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
					  subStrs=subStrs2;
				  }else{
					  String subStrs2[] = {currentLine};
					  subStrs=subStrs2;
				  }
				  
				  for (int j = 0; j < subStrs.length; j++) {				  
					  String currentToken = subStrs[j];
					  
					  
					  if(currentToken.equals("")&&firstGram!=true){
						  continue;  
					  }
					  previous = current;
					  current = currentToken;
					  
					  if(firstGram){

						  if (current.equals("")) {
							continue;
						  }
						  if (current.contains("endindicator")) {
							  mapListUnit[0]=current.replaceAll("endindicator", "");
//							  mapListUnit[0]=current;
						  }else{
							  mapListUnit[0]=current;
//						  	  mapListUnit[0]="[Curr:"+current+"]";
						  }
						  
						  firstGram=false;
					  }else{
						  if (current.contains("endindicator")) {
							  if (current.replaceAll("endindicator", "").trim().length()<=0) {
								  continue;
							  }else{
								  mapListUnit[0]=previous +" "+ current.replaceAll("endindicator", "");
//							  	  mapListUnit[0]=previous +" "+ current;
							  }
						  }else{
							  if (current.trim().length() <= 0) {
								  continue;
							  }else{
								  mapListUnit[0]=previous+" "+current;
//								  mapListUnit[0]="[Pre:"+previous+"]"+" "+"[Curr:"+current+"]";
							  }
							  
						  }
					  }
					  
					  if (BySyring.equals("year")) {
						  mapkey.set(mapListUnit[0]+"\t"+mapListUnit[1]);
						  valueTuple.set(mapListUnit[2]+","+mapListUnit[3]);
					  }else if(BySyring.equals("lastName")){
						  mapkey.set(mapListUnit[0]+"\t"+mapListUnit[3]);
						  valueTuple.set(mapListUnit[2]+","+mapListUnit[1]);
					  }
					  
					  if (current.contains("endindicator")) {
						  context.write(mapkey, valueTuple);
						  
						  if (current.equalsIgnoreCase("endindicator")) {
							  continue;
						  }else{
							  if (BySyring.equals("year")) {
								  context.write(new Text(current.replaceAll("endindicator", "")+" -end-"+"\t"+mapListUnit[1]), valueTuple);
//								  context.write(new Text(current+" END"+mapListUnit[1]), valueTuple);
							  }else if(BySyring.equals("lastName")){
								  context.write(new Text(current.replaceAll("endindicator", "")+" -end-"+"\t"+mapListUnit[3]), valueTuple);
//								  context.write(new Text(current+" END"+mapListUnit[3]), valueTuple);
							  }
						  }
						  
						  current = "-start-";
						  
					  }else{
						  context.write(mapkey, valueTuple);
					  }
				  }
			  }
		  }
//		  if (BySyring.equals("year")) {
//			  mapkey.set(current+" -end-"+"\t"+mapListUnit[1]);
//			  context.write(mapkey, valueTuple);
//		  }else if(BySyring.equals("lastName")){
//			  mapkey.set(current+" -end-"+"\t"+mapListUnit[3]);
//			  context.write(mapkey, valueTuple);
//		  }
	}
}
