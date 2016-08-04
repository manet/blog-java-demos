package org.example.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 Manet Yim
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.F
 * 
 * @author Manet Yim
 *
 */
public class AssetData {
	
	// apply singleton access to this class
	private static AssetData instance;
	public static AssetData getInstance(){
		return (instance == null ? new AssetData(): instance);
	}
	// end singleton
	
	private final String DATA_PATH = "assets/data";
	
	// list all data files as applicable
	private final File loremTextFile = new File(DATA_PATH + "/lorem.txt");
	private final File peopleJsonFile = new File(DATA_PATH + "/people.json");
	// free.text file is open to realtime modification
	private File freeTextFile;
	// end all data files

	public File getFreeTextFile() {
		freeTextFile = new File(DATA_PATH + "/free.txt");
		return freeTextFile;
	}

	public void setFreeTextFile(File freeTextFile) {
		this.freeTextFile = freeTextFile;
	}
	
	public File getLoremTextFile() {
		return loremTextFile;
	}

	public File getPeopleJsonFile() {
		return peopleJsonFile;
	}
	
	public String getLoremTextFileName (){
		return loremTextFile.getName();
	}
	
	public StringBuffer getFreeTextContent(){
		List<String> lines = this.getLinesFromFile(this.getFreeTextFile());
		StringBuffer content = new StringBuffer();
		for (String line : lines){
			content.append(line != "\n" ? line.concat("\n") : line);
		}
		return content;
	}

	public List<String> getLoremTextLines(){
		return this.getLinesFromFile(loremTextFile);
	}
	
	public StringBuffer getLoremTextContent(){
		List<String> lines = this.getLinesFromFile(loremTextFile);
		StringBuffer content = new StringBuffer();
		for (String line : lines){
			content.append(line != "\n" ? line.concat("\n") : line);
		}
		return content;
	}

	public List<Person> getPeople(){
		Gson gson = new Gson();
		List<Person> people = null;
		try {
			people = gson.fromJson(new FileReader(peopleJsonFile), new TypeToken<List<Person>>() {}.getType());
			Collections.shuffle(people);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return people;
	}

	public Person getPersonById(Integer id) {
		for (Person person : this.getPeople()) {
			if (person.getId() == id) {
				return person;
			}
		}
		return null;
	}
	
	private List<String> getLinesFromFile(File file){
		List<String> contents = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = reader.readLine()) != null){
				contents.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contents;
	}
}
