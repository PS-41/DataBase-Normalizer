import java.util.*;
import java.io.*;

class CandidateKey
{
	static Set<String> super_key = new HashSet<String>();
	void func(String arr[], Dictionary vis, String data[], String left[], String right[])
	{
		int no_of_attributes = arr.length;
		int num_fd = left.length;
		int ch = 1;
		for (int j = 0; j < no_of_attributes; j++)
		{
			if ((int)vis.get(arr[j]) == 0)
			{
				ch = 0;
				break;
			}
		}
		if (ch == 1) return;
		for (int i = 0; i < num_fd; i++)
		{
			int check = 1;
			for (int j = 0; j < no_of_attributes; j++)
			{
				if ((int)vis.get(arr[j]) == 0)
				{
					check = 0;
					break;
				}
			}
			if (check == 1) 
			{
				break;
			}
			check = 1;
			StringTokenizer st1 = new StringTokenizer(left[i], ",");
	        while (st1.hasMoreTokens())
	        { 
	        	String tmp = st1.nextToken();
	        	if ((int)vis.get(tmp) == 0)
				{
					check = 0;
					break;
				}
			}
			if (check == 1)
			{
				int rec = 0;
				StringTokenizer st2 = new StringTokenizer(right[i], ",");
		        while (st2.hasMoreTokens())
		        { 
		        	String tmp = st2.nextToken();
		        	if ((int)vis.get(tmp) == 0) rec = 1;
					vis.put(tmp, 1);
				}
				if (rec == 1) func(arr, vis, data, left, right);
			}
			
		}
		int chh = 1;
		for (int j = 0; j < no_of_attributes; j++)
		{
			if ((int)vis.get(arr[j]) == 0)
			{
				chh = 0;
				break;
			}
		}
		if (chh == 1)
		{
			String tmp = "";
			for (int i = 0; i < data.length; i++)
			{	
				tmp += data[i] + ",";
			}
			super_key.add(tmp);
		}	
	}
}


class NormalForms
{
	static int bcnf = 1;
	static int nf3 = 1;
	static int nf2 = 1;
	void check_func(String attributes[], String left[], String right[], Set<String> candidate_key)
	{
		int no_of_attributes = attributes.length;
		
		// making dictionary of prime attributes
		Dictionary prime_attributes = new Hashtable();
		for (int i = 0; i < no_of_attributes; i++) prime_attributes.put(attributes[i], 0);
		for(String key : candidate_key)
		{
			StringTokenizer st1 = new StringTokenizer(key, ",");
			while (st1.hasMoreTokens()) prime_attributes.put(st1.nextToken(), 1);
		}
		
		// 2-NF CHECK KRUNGA :( :(
		int partial_dependency = 0;

		for (int i = 0; i < right.length; i++)
		{
			String tmp1 = right[i];
			StringTokenizer st1 = new StringTokenizer(tmp1, ","); 
	        while (st1.hasMoreTokens())
	        { 
	        	if ((int)prime_attributes.get(st1.nextToken()) == 0)
	        	{
	        		String tmp2 = left[i];
	        		for(String key : candidate_key)
					{
						Dictionary dic3 = new Hashtable();
						Dictionary dic4 = new Hashtable();
						for (int j = 0; j < no_of_attributes; j++) 
						{
							dic3.put(attributes[j], 0); // for candidate key
							dic4.put(attributes[j], 0); // for left hand side of fd
						}
						StringTokenizer st2 = new StringTokenizer(key, ","); 
	        			while (st2.hasMoreTokens())
	        			{
	        				dic3.put(st2.nextToken(), 1);
	        			}
	        			StringTokenizer st3 = new StringTokenizer(tmp2, ","); 
	        			while (st3.hasMoreTokens())
	        			{
	        				dic4.put(st3.nextToken(), 1);
	        			}

	        			int ch1 = 0;
	        			int ch2 = 0;

	        			for (int j = 0; j < no_of_attributes; j++)
	        			{
	        				if ((int)dic3.get(attributes[j]) == 0 && (int)dic4.get(attributes[j]) == 1) ch1 = 1;
	        				if ((int)dic3.get(attributes[j]) == 1 && (int)dic4.get(attributes[j]) == 0) ch2 = 1;
	        			}

	        			if (ch1 == 0 && ch2 == 1)
	        			{ 
	        				partial_dependency = 1;
	        				break;
	        			}
					}
	        	}
	        	if (partial_dependency == 1) break;
	        }
	        if (partial_dependency == 1) break;
		}

		if (partial_dependency == 1) nf2 = 0;






		// 3-NF CHECK KRUNGA AB.. BETTER :)
		for (int i = 0; i < right.length; i++)
		{
			String tmp1 = left[i] + ",";
			int ch1 = 0;
			for(String key : CandidateKey.super_key)
			{
				if (tmp1.equals(key))
				{
					ch1 = 1;
					break;
				}
			}
			if (ch1 == 1) continue;
			int ch2 = 0;
			String tmp2 = right[i];
			StringTokenizer st1 = new StringTokenizer(tmp2, ","); 
	        while (st1.hasMoreTokens())
	        {
	        	if ((int)prime_attributes.get(st1.nextToken()) == 0)
	        	{
	        		ch2 = 1;
	        		break;
	        	}
	        }
	        if (ch2 == 1)
	        {
	        	nf3 = 0;
	        	break;
	        }
		}



		// BCNF CHECK KRUNGA AB... EZ PZ :)
		for (int i = 0; i < right.length; i++)
		{
			String tmp1 = left[i] + ",";
			int ch1 = 0;
			for(String key : CandidateKey.super_key)
			{
				if (tmp1.equals(key))
				{
					ch1 = 1;
					break;
				}
			}
			if (ch1 == 0)
			{
				bcnf = 0;
				break;
			}
		}
	}
}



class Decompose
{
	static Set<String> relation = new HashSet<String>();
	static Set<String> functional_dependency = new HashSet<String>();
	static Set<String> candidate = new HashSet<String>();
	static int c = 0;
	
	int propersubset(String tmp2, String key, String[] attributes)
	{
		int no_of_attributes = attributes.length;
		int proper_subset = 0;
		Dictionary dic3 = new Hashtable();
		Dictionary dic4 = new Hashtable();
		for (int j = 0; j < no_of_attributes; j++) 
		{
			dic3.put(attributes[j], 0); // for candidate key
			dic4.put(attributes[j], 0); // for left hand side of fd
		}
		StringTokenizer st2 = new StringTokenizer(key, ","); 
		while (st2.hasMoreTokens())
		{
			dic3.put(st2.nextToken(), 1);
		}
		StringTokenizer st3 = new StringTokenizer(tmp2, ","); 
		while (st3.hasMoreTokens())
		{
			dic4.put(st3.nextToken(), 1);
		}

		int ch1 = 0;
		int ch2 = 0;

		for (int j = 0; j < no_of_attributes; j++)
		{
			if ((int)dic3.get(attributes[j]) == 0 && (int)dic4.get(attributes[j]) == 1) ch1 = 1;
			if ((int)dic3.get(attributes[j]) == 1 && (int)dic4.get(attributes[j]) == 0) ch2 = 1;
		}

		if (ch1 == 0 && ch2 == 1)
		{
			proper_subset = 1;
		}
		return proper_subset;
	}

	void decompose_to_2NF(String attributes[], String left[], String right[], Set<String> candidate_key)
	{
		int no_of_attributes = attributes.length;
		int num_fd = left.length;
		int[] left_dependency = new int[num_fd];
		for (int i = 0; i < num_fd; i++) left_dependency[i] = 1;
	
		for (int i = 0; i < num_fd; i++)
		{
    		String tmp2 = left[i];
    		if (left_dependency[i] == 0) continue;
    		int proper_subset = 0;
    		for(String key : candidate_key)
			{
				proper_subset = propersubset(tmp2, key, attributes);
				if (proper_subset == 1) break;
    		}
    		if (proper_subset == 1)
			{	
				Set<String> y_plus = new HashSet<String>();
				StringTokenizer st1 = new StringTokenizer(left[i] + "," + right[i], ",");
				while (st1.hasMoreTokens()) y_plus.add(st1.nextToken());
				String depend = "";
				left_dependency[i] = 0;
				depend += left[i] + ">" + right[i] + ";";
				String tmp_key = left[i];
				while (true)
				{
					Set<String> old_y_plus = new HashSet<String>();
					for(String keyy : y_plus)
					{
					   old_y_plus.add(keyy);
					}
					for (int j = 0; j < num_fd; j++)
					{
						if (left_dependency[j] == 0) continue;
						if (propersubset(left[j], tmp_key, attributes) == 1) continue;
						StringTokenizer ps1 = new StringTokenizer(left[j], ",");
						int checko = 1;
						while(ps1.hasMoreTokens())
						{
							int check1 = 0;
							String s1 = ps1.nextToken();
							for (String key: y_plus)
							{
								if (s1.equals(key))
								{
									check1 = 1;
									break;
								}
							}
							if (check1 == 0)
							{
								checko = 0;
								break;
							}
						}
						if (checko == 1)
						{
							left_dependency[j] = 0;
							depend += left[j] + ">" + right[j] + ";";
							StringTokenizer ps2 = new StringTokenizer(left[j] + "," + right[j], ",");
							while (ps2.hasMoreTokens())
							{
								y_plus.add(ps2.nextToken());
							}
						}
					}
					if (y_plus.size() == old_y_plus.size()) break;
				}

				String rela = "";
				for (String key: y_plus)
				{
					rela += key + ",";
				}




				Set<String> atta = new HashSet<String>();
				StringTokenizer st1010 = new StringTokenizer(depend, ";");
				String newdepend = "";
				while (st1010.hasMoreTokens())
				{
					String tmp1 = st1010.nextToken();
					StringTokenizer st101 = new StringTokenizer(tmp1, ">");
					String tmpp2 = st101.nextToken();
					String tmp3 = st101.nextToken();
					String ooo = "";
					for (int z = 0; z < left.length; z++)
					{
						if (propersubset(left[z], tmpp2 + ",", attributes) == 1)
						{
							StringTokenizer st103 = new StringTokenizer(right[z], ",");
							while (st103.hasMoreTokens())
							{
								String tmp4 = st103.nextToken();
								StringTokenizer st104 = new StringTokenizer(tmp3, ",");
								while (st104.hasMoreTokens())
								{
									if (tmp4.equals(st104.nextToken()))
									{
										ooo += tmp4 + ",";
										break;
									}
								}
							}
						}
					}
					String yoyo = "";
					StringTokenizer st107 = new StringTokenizer(tmp3, ",");
					while (st107.hasMoreTokens())
					{
						int found = 0;
						String tmp7 = st107.nextToken();
						StringTokenizer st108 = new StringTokenizer(ooo, ",");
						while (st108.hasMoreTokens())
						{
							if (tmp7.equals(st108.nextToken()))
							{
								found = 1;
								break;
							}
						}
						if (found == 0) yoyo += tmp7 + ",";
					}
					String newyoyo = "";
					for (int z = 0; z < yoyo.length() - 1; z++) newyoyo += yoyo.charAt(z);
					if (newyoyo.length() > 0) newdepend += tmpp2 + ">" + newyoyo + ";";
					StringTokenizer asl = new StringTokenizer(tmpp2, ",");
					while (asl.hasMoreTokens()) atta.add(asl.nextToken());
				}

				if (newdepend.length() > 0)
				{
					StringTokenizer nameless = new StringTokenizer(newdepend, ";");
					String newrela = "";
					while (nameless.hasMoreTokens())
					{
						StringTokenizer st110 = new StringTokenizer(nameless.nextToken(), ">");
						StringTokenizer st111 = new StringTokenizer(st110.nextToken() + "," + st110.nextToken(), ",");
						while (st111.hasMoreTokens()) atta.add(st111.nextToken());
					}
					for (String kkey: atta) newrela += kkey + ",";
					relation.add(Integer.toString(c) + ":" + newrela);
					functional_dependency.add(Integer.toString(c) + ":" + newdepend);
					c++;
				}
			}
		}


		for (String KEY: candidate_key)
		{
			Set<String> y_plus = new HashSet<String>();
			String depend = "";
			StringTokenizer ps4 = new StringTokenizer(KEY, ",");
			while (ps4.hasMoreTokens()) y_plus.add(ps4.nextToken());
			for (int i = 0; i < num_fd; i++)
			{
				if (left_dependency[i] == 0) continue;
				String cm1 = "";
				StringTokenizer ps1 = new StringTokenizer(left[i], ",");
				while (ps1.hasMoreTokens()) cm1 += ps1.nextToken();
				int check2 = 0;
				for (String key: candidate_key)
				{
					String cm2 = "";
					StringTokenizer ps2 = new StringTokenizer(key, ",");
					while (ps2.hasMoreTokens()) cm2 += ps2.nextToken();
					if (cm1.equals(cm2))
					{
						check2 = 1;
						break;
					}
				}
				if (check2 == 1)
				{
					StringTokenizer st1 = new StringTokenizer(left[i] + "," + right[i], ",");
					while (st1.hasMoreTokens()) y_plus.add(st1.nextToken());
					left_dependency[i] = 0;
					depend += left[i] + ">" + right[i] + ";";
					String tmp_key = left[i];
					while (true)
					{
						Set<String> old_y_plus = new HashSet<String>();
						for(String keyy : y_plus)
						{
						   old_y_plus.add(keyy);
						}
						for (int j = 0; j < num_fd; j++)
						{
							if (left_dependency[j] == 0) continue;
							if (propersubset(left[j], tmp_key, attributes) == 1) continue;
							StringTokenizer ps3 = new StringTokenizer(left[j], ",");
							int checko = 1;
							while(ps3.hasMoreTokens())
							{
								int check1 = 0;
								String s1 = ps3.nextToken();
								for (String key: y_plus)
								{
									if (s1.equals(key))
									{
										check1 = 1;
										break;
									}
								}
								if (check1 == 0)
								{
									checko = 0;
									break;
								}
							}
							if (checko == 1)
							{
								left_dependency[j] = 0;
								depend += left[j] + ">" + right[j] + ";";
								StringTokenizer ps2 = new StringTokenizer(left[j] + "," + right[j], ",");
								while (ps2.hasMoreTokens())
								{
									y_plus.add(ps2.nextToken());
								}
							}
						}
						if (y_plus.size() == old_y_plus.size()) break;
					}
				}
			}
			int tired1 = 1;
			for (String key1: relation)
			{
				int tired2 = 1;
				String temp1 = "";
				String temp2 = "";
				int check1 = 0;
				for (int i = 0; i < key1.length(); i++)
				{
					if (key1.charAt(i) == ':')
					{
						check1 = 1;
						continue;
					}
					if (check1 == 0) temp1 += key1.charAt(i);
					if (check1 == 1) temp2 += key1.charAt(i);
				}
				for (String key2: y_plus)
				{
					int tired3 = 0;
					StringTokenizer ps30 = new StringTokenizer(temp2, ",");
					while (ps30.hasMoreTokens())
					{
						if (key2.equals(ps30.nextToken()))
						{
							tired3 = 1;
							break;
						}
					}
					if (tired3 == 0)
					{
						tired2 = 0;
						break;
					}
				}
				if (tired2 == 1)
				{
					tired1 = 0;
					break;
				}
			}

			if (tired1 == 1)
			{
				String rela = "";
				for (String key: y_plus)
				{
					rela += key + ",";
				}

				Set<String> atta = new HashSet<String>();
				StringTokenizer st1010 = new StringTokenizer(depend, ";");
				String newdepend = "";
				while (st1010.hasMoreTokens())
				{
					String tmp1 = st1010.nextToken();
					StringTokenizer st101 = new StringTokenizer(tmp1, ">");
					String tmp2 = st101.nextToken();
					String tmp3 = st101.nextToken();
					String ooo = "";
					for (int z = 0; z < left.length; z++)
					{
						if (propersubset(left[z], tmp2 + ",", attributes) == 1)
						{
							StringTokenizer st103 = new StringTokenizer(right[z], ",");
							while (st103.hasMoreTokens())
							{
								String tmp4 = st103.nextToken();
								StringTokenizer st104 = new StringTokenizer(tmp3, ",");
								while (st104.hasMoreTokens())
								{
									if (tmp4.equals(st104.nextToken()))
									{
										ooo += tmp4 + ",";
										break;
									}
								}
							}
						}
					}
					String yoyo = "";
					StringTokenizer st107 = new StringTokenizer(tmp3, ",");
					while (st107.hasMoreTokens())
					{
						int found = 0;
						String tmp7 = st107.nextToken();
						StringTokenizer st108 = new StringTokenizer(ooo, ",");
						while (st108.hasMoreTokens())
						{
							if (tmp7.equals(st108.nextToken()))
							{
								found = 1;
								break;
							}
						}
						if (found == 0) yoyo += tmp7 + ",";
					}
					String newyoyo = "";
					for (int z = 0; z < yoyo.length() - 1; z++) newyoyo += yoyo.charAt(z);
					if (newyoyo.length() > 0) newdepend += tmp2 + ">" + newyoyo + ";";
				}
				StringTokenizer asl = new StringTokenizer(KEY, ",");
				while (asl.hasMoreTokens()) atta.add(asl.nextToken());
				StringTokenizer nameless = new StringTokenizer(newdepend, ";");
				String newrela = "";
				while (nameless.hasMoreTokens())
				{
					StringTokenizer st110 = new StringTokenizer(nameless.nextToken(), ">");
					StringTokenizer st111 = new StringTokenizer(st110.nextToken() + "," + st110.nextToken(), ",");
					while (st111.hasMoreTokens()) atta.add(st111.nextToken());
				}
				for (String kkey: atta) newrela += kkey + ",";
				relation.add(Integer.toString(c) + ":" + newrela);
				functional_dependency.add(Integer.toString(c) + ":" + newdepend);
				c++;
			}
		}
		



		for (int i = 0; i < num_fd; i++)
		{
			if (left_dependency[i] == 0) continue;
			Set<String> y_plus = new HashSet<String>();
			StringTokenizer st1 = new StringTokenizer(left[i] + "," + right[i], ",");
			while (st1.hasMoreTokens()) y_plus.add(st1.nextToken());
			String depend = "";
			left_dependency[i] = 0;
			depend += left[i] + ">" + right[i] + ";";
			String tmp_key = left[i];
			while (true)
			{
				Set<String> old_y_plus = new HashSet<String>();
				for(String keyy : y_plus)
				{
				   old_y_plus.add(keyy);
				}
				for (int j = 0; j < num_fd; j++)
				{
					if (left_dependency[j] == 0) continue;
					if (propersubset(left[j], tmp_key, attributes) == 1) continue;
					StringTokenizer ps1 = new StringTokenizer(left[j], ",");
					int checko = 1;
					while(ps1.hasMoreTokens())
					{
						int check1 = 0;
						String s1 = ps1.nextToken();
						for (String key: y_plus)
						{
							if (s1.equals(key))
							{
								check1 = 1;
								break;
							}
						}
						if (check1 == 0)
						{
							checko = 0;
							break;
						}
					}
					if (checko == 1)
					{
						left_dependency[j] = 0;
						depend += left[j] + ">" + right[j] + ";";
						StringTokenizer ps2 = new StringTokenizer(left[j] + "," + right[j], ",");
						while (ps2.hasMoreTokens())
						{
							y_plus.add(ps2.nextToken());
						}
					}
				}
				if (y_plus.size() == old_y_plus.size()) break;
			}

			String rela = "";
			for (String key: y_plus)
			{
				rela += key + ",";
			}



			Set<String> atta = new HashSet<String>();
			StringTokenizer st1010 = new StringTokenizer(depend, ";");
			String newdepend = "";
			while (st1010.hasMoreTokens())
			{
				String tmp1 = st1010.nextToken();
				StringTokenizer st101 = new StringTokenizer(tmp1, ">");
				String tmpp2 = st101.nextToken();
				String tmp3 = st101.nextToken();
				String ooo = "";
				for (int z = 0; z < left.length; z++)
				{
					if (propersubset(left[z], tmpp2 + ",", attributes) == 1)
					{
						StringTokenizer st103 = new StringTokenizer(right[z], ",");
						while (st103.hasMoreTokens())
						{
							String tmp4 = st103.nextToken();
							StringTokenizer st104 = new StringTokenizer(tmp3, ",");
							while (st104.hasMoreTokens())
							{
								if (tmp4.equals(st104.nextToken()))
								{
									ooo += tmp4 + ",";
									break;
								}
							}
						}
					}
				}
				String yoyo = "";
				StringTokenizer st107 = new StringTokenizer(tmp3, ",");
				while (st107.hasMoreTokens())
				{
					int found = 0;
					String tmp7 = st107.nextToken();
					StringTokenizer st108 = new StringTokenizer(ooo, ",");
					while (st108.hasMoreTokens())
					{
						if (tmp7.equals(st108.nextToken()))
						{
							found = 1;
							break;
						}
					}
					if (found == 0) yoyo += tmp7 + ",";
				}
				String newyoyo = "";
				for (int z = 0; z < yoyo.length() - 1; z++) newyoyo += yoyo.charAt(z);
				if (newyoyo.length() > 0) newdepend += tmpp2 + ">" + newyoyo + ";";
				StringTokenizer asl = new StringTokenizer(tmpp2, ",");
				while (asl.hasMoreTokens()) atta.add(asl.nextToken());
			}

			if (newdepend.length() > 0)
			{
				StringTokenizer nameless = new StringTokenizer(newdepend, ";");
				String newrela = "";
				while (nameless.hasMoreTokens())
				{
					StringTokenizer st110 = new StringTokenizer(nameless.nextToken(), ">");
					StringTokenizer st111 = new StringTokenizer(st110.nextToken() + "," + st110.nextToken(), ",");
					while (st111.hasMoreTokens()) atta.add(st111.nextToken());
				}
				for (String kkey: atta) newrela += kkey + ",";
				relation.add(Integer.toString(c) + ":" + newrela);
				functional_dependency.add(Integer.toString(c) + ":" + newdepend);
				c++;
			}
		}
	}

	void decompose_to_3NF(String attributes[], String left[], String right[], Set<String> candidate_key)
	{
		c = 0;
		relation.clear();
		functional_dependency.clear();
		int[] left_dependency = new int[left.length];
		for (int i = 0; i < left.length; i++) left_dependency[i] = 1;
		
		for (int i = 0; i < left.length; i++)
		{
			if (left_dependency[i] == 0) continue;
			String rela = "";
			String depend = "";
			for (int j = 0; j < left.length; j++)
			{
				if (left[i].equals(left[j]))
				{
					left_dependency[j] = 0;
					rela += right[j] + ",";
				}
			}
			String relus = "";
			for (int j = 0; j < rela.length() - 1; j++) relus += rela.charAt(j);
			depend = left[i] + ">" + relus + ";";
			rela = left[i] + "," + rela;

			relation.add(Integer.toString(c) + ":" + rela);

			functional_dependency.add(Integer.toString(c) + ":" + depend);

			c++;
		}
		int ch3 = 0;
		for (String key: candidate_key)
		{
			for (String key2: relation)
			{
				int ch2 = 1;
				StringTokenizer st1 = new StringTokenizer(key, ",");
				while (st1.hasMoreTokens())
				{
					String qq = st1.nextToken();
					StringTokenizer st2 = new StringTokenizer(key2, ",");
					int ch1 = 0;
					while (st2.hasMoreTokens())
					{
						if (qq.equals(st2.nextToken()))
						{
							ch1 = 1;
							break;
						}
					}
					if (ch1 == 0)
					{
						ch2 = 0;
						break;
					}
				}
				if (ch2 == 1)
				{
					ch3 = 1;
					break;
				}
			}
			if (ch3 == 1) break;
		}
		if (ch3 == 0)
		{
			String rela = "";
			String depend = "";
			for (String key: candidate_key)
			{
				rela += key;
				break;
			}
			relation.add(Integer.toString(c) + ":" + rela);

			functional_dependency.add(Integer.toString(c) + ":" + depend);

			c++;
		}
	}

	void decompose_to_BCNF(String attributes_[], String left_[], String right_[], Set<String> candidate_key_)
	{
		c = 0;
		relation.clear();
		functional_dependency.clear();
		candidate.clear();
		String rela = "";
		for (int i = 0; i < attributes_.length; i++) rela += attributes_[i] + ",";
		String depend = "";
		for (int i = 0; i < left_.length; i++) depend += left_[i] + ">" + right_[i] + ";";

		relation.add(Integer.toString(c) + ":" + rela);

		functional_dependency.add(Integer.toString(c) + ":" + depend);
		c++;

		
		while (true)
		{
			int tired1 = 1;
			for(String key : relation)
			{
				int tired2 = 1;
				String temp1 = "";
				String temp2 = "";
				int check1 = 0;
				for (int i = 0; i < key.length(); i++)
				{
					if (key.charAt(i) == ':')
					{
						check1 = 1;
						continue;
					}
					if (check1 == 0) temp1 += key.charAt(i);
					if (check1 == 1) temp2 += key.charAt(i);
				}
				int no_of_attributes = 0;
				for (int i = 0; i < temp2.length(); i++)
				{
					if (temp2.charAt(i) == ',') no_of_attributes++;
				}
				String[] attributes = new String[no_of_attributes];
				int x = 0;
				StringTokenizer st1 = new StringTokenizer(temp2, ",");
				while (st1.hasMoreTokens()) attributes[x++] = st1.nextToken();
				for (String key2: functional_dependency)
				{
					String tempp1 = "";
					String tempp2 = "";
					int checkk1 = 0;
					for (int j = 0; j < key2.length(); j++)
					{
						if (key2.charAt(j) == ':')
						{
							checkk1 = 1;
							continue;
						}
						if (checkk1 == 0) tempp1 += key2.charAt(j);
						if (checkk1 == 1) tempp2 += key2.charAt(j);
					}
					if (Integer.parseInt(tempp1) == Integer.parseInt(temp1))
					{
						int num_fd = 0;
				        for (int i = 0; i < tempp2.length(); i++) if (tempp2.charAt(i) == ';') num_fd++;
				 
				        String[] left = new String[num_fd];
				        String[] right = new String[num_fd];

				        StringTokenizer st2 = new StringTokenizer(tempp2, ";"); 
				        x = 0;
				        while (st2.hasMoreTokens()) 
				        {
				        	String st3 = st2.nextToken();
				        	StringTokenizer st4 = new StringTokenizer(st3, ">");
				        	left[x] = st4.nextToken();
				        	right[x++] = st4.nextToken();
				        }

				        CandidateKey.super_key.clear();

						String tmp3 = "";
						for (int i = 0; i < no_of_attributes; i++)
						{
							tmp3 += attributes[i] + ",";
							Demo ddd = new Demo();
							ddd.printCombination(attributes, no_of_attributes, i + 1, left, right);
						}

						CandidateKey.super_key.add(tmp3);

						Set<String> candidate_key = new HashSet<String>();
						
						for(String key3 : CandidateKey.super_key)
						{
							int ps = 0;
							int ck = 1;
							for (int i = 0; i < key3.length(); i++)
							{
								if (key3.charAt(i) == ',') ps -= -1;
							}
							String[] set = new String[ps];
							StringTokenizer st3 = new StringTokenizer(key3, ",");
							x = 0;
							while (st3.hasMoreTokens()) set[x++] = st3.nextToken();
				 
					        for (int i = 1; i < (1 << ps) - 1; i++) 
					        { 
					            String tmp4 = "";
					            for (int j = 0; j < ps; j++)
					                if ((i & (1 << j)) > 0) 
					                	tmp4 += set[j] + ",";

					            for (String key4: CandidateKey.super_key)
					            {
					            	if (tmp4.equals(key4))
					            	{
					            		ck = 0;
					            		break;
					            	}
					            }
					            if (ck == 0) break;
					        }
					        if (ck == 1) candidate_key.add(key3);
						}
						NormalForms n = new NormalForms();
						n.bcnf = 1;

						n.check_func(attributes, left, right, candidate_key);
						if (n.bcnf == 0)
						{
							tired2 = 0;
							for (int i = 0; i < right.length; i++)
							{
								String tmp1 = left[i] + ",";
								int ch1 = 0;
								for(String key10 : CandidateKey.super_key)
								{
									if (tmp1.equals(key10))
									{
										ch1 = 1;
										break;
									}
								}
								if (ch1 == 0)
								{
									rela = "";
									depend = "";
									
									relation.remove(temp1 + ":" + temp2);
									functional_dependency.remove(tempp1 + ":" + tempp2);
									

									rela += left[i] + "," + right[i] + ",";
									relation.add(temp1 + ":" + rela);

									StringTokenizer ps24 = new StringTokenizer(tempp2, ";");
									while (ps24.hasMoreTokens())
									{
										int check21 = 1;
										String sp21 = ps24.nextToken();
										StringTokenizer ps25 = new StringTokenizer(sp21, ">");
										String sp22 = ps25.nextToken() + "," + ps25.nextToken();
										StringTokenizer ps26 = new StringTokenizer(sp22, ",");
										while (ps26.hasMoreTokens())
										{
											StringTokenizer ps27 = new StringTokenizer(rela, ",");
											String sp3 = ps26.nextToken();
											int check10 = 0;
											while (ps27.hasMoreTokens())
											{
												if (sp3.equals(ps27.nextToken()))
												{
													check10 = 1;
													break;
												}
											}
											if (check10 == 0)
											{
												check21 = 0;
												break;
											}
										}
										if (check21 == 1)
										{
											depend += sp21 + ";";
										}
									}
									functional_dependency.add(temp1 + ":" + depend);
									
									rela = "";
									depend = "";
									StringTokenizer ps11 = new StringTokenizer(temp2, ",");
									while (ps11.hasMoreTokens())
									{
										String ps12 = ps11.nextToken();
										StringTokenizer ps13 = new StringTokenizer(right[i], ",");
										int present = 0;
										while(ps13.hasMoreTokens())
										{
											if (ps12.equals(ps13.nextToken()))
											{
												present = 1;
												break;
											}
										}
										if (present == 0) rela += ps12 + ",";
									}

									StringTokenizer ps14 = new StringTokenizer(tempp2, ";");
									// System.out.println("yoyo honey singh = " + tempp2);
									while (ps14.hasMoreTokens())
									{
										int check11 = 1;
										String sp1 = ps14.nextToken();
										StringTokenizer ps15 = new StringTokenizer(sp1, ">");
										
										String sp2 = ps15.nextToken();
										StringTokenizer ps16 = new StringTokenizer(sp2, ",");
										while (ps16.hasMoreTokens())
										{
											StringTokenizer ps17 = new StringTokenizer(rela, ",");
											String sp3 = ps16.nextToken();
											int check10 = 0;
											while (ps17.hasMoreTokens())
											{
												if (sp3.equals(ps17.nextToken()))
												{
													check10 = 1;
													break;
												}
											}
											if (check10 == 0)
											{
												check11 = 0;
												break;
											}
										}
										if (check11 == 1)
										{
											String tulip = "";
											String sp71 = ps15.nextToken();
											StringTokenizer ps71 = new StringTokenizer(sp71, ",");
											while (ps71.hasMoreTokens())
											{
												StringTokenizer ps17 = new StringTokenizer(rela, ",");
												String sp3 = ps71.nextToken();
												while (ps17.hasMoreTokens())
												{
													if (sp3.equals(ps17.nextToken())) tulip += sp3 + ",";
												}
											}

											String newtulip = "";
											for (int z = 0; z < tulip.length() - 1; z++) newtulip += tulip.charAt(z);
											if (newtulip.length() > 0) depend += sp2 + ">" + newtulip + ";";
										}
									}
									relation.add(Integer.toString(c) + ":" + rela);

									functional_dependency.add(Integer.toString(c) + ":" + depend);

									c++;
									break;
								}
							}
						}
						else
						{
							String ps93 = "";
							for (String ps94: candidate_key)
							{
								ps93 += ps94 + ".";
							}
							candidate.add(tempp1 + ":" + ps93);
						}
						break;
					}
				}
				if (tired2 == 0)
				{
					tired1 = 0;
					break;
				}
			}
			if (tired1 == 1) break;
		}

		for (String key1: candidate_key_)
		{
			int present = 0;
			for (String key2: relation)
			{
				int found = 1;
				StringTokenizer st1 = new StringTokenizer(key2, ":");
				st1.nextToken();
				String sp49 = st1.nextToken();
				StringTokenizer st2 = new StringTokenizer(key1, ",");
				while(st2.hasMoreTokens())
				{
					String sp50 = st2.nextToken();
					StringTokenizer st3 = new StringTokenizer(sp49, ",");
					int foun = 0;
					while (st3.hasMoreTokens())
					{
						if (sp50.equals(st3.nextToken()))
						{
							foun = 1;
							break;
						}
					}
					if (foun == 0)
					{
						found = 0;
						break;
					}
				}
				if (found == 1)
				{
					present = 1;
					break;
				}
			}
			if (present == 0)
			{
				relation.add(Integer.toString(c) + ":" + key1);
				functional_dependency.add(Integer.toString(c) + ":" + "");
				candidate.add(Integer.toString(c) + ":" + key1);
				c++;
			}
		}
	}
}


class Demo
{
	static void combinationUtil(String arr[], int n, int r, int index, String data[], int i, String left[], String right[]) 
    {
    	int no_of_attributes = arr.length;
        if (index == r) 
        {
        	Dictionary vis = new Hashtable(); 
        	for (int j = 0; j < no_of_attributes; j++)
        	{
        		vis.put(arr[j], 0);
        	}
        	for (int j = 0; j < r; j++) vis.put(data[j], 1); 
        	CandidateKey c = new CandidateKey();
        	c.func(arr, vis, data, left, right);
            return; 
        }
        if (i >= n) 
            return; 
        data[index] = arr[i]; 
        combinationUtil(arr, n, r, index + 1, data, i + 1, left, right); 
        combinationUtil(arr, n, r, index, data, i + 1, left, right); 
    }
    static void printCombination(String arr[], int n, int r, String left[], String right[]) 
    {
        String data[] = new String[r];
        combinationUtil(arr, n, r, 0, data, 0, left, right); 
    }
    public static void main(String[] args) 
    {
    	Scanner sc = new Scanner(System.in);
    	int no_of_attributes = 1;
       	System.out.println("Enter space separated attribute names:");
		String tmp1 = sc.nextLine();
		for (int i = 0; i < tmp1.length(); i++) if (tmp1.charAt(i) == ' ') no_of_attributes++;
		String[] attributes = new String [no_of_attributes];
		StringTokenizer st1 = new StringTokenizer(tmp1, " "); 
        int x = 0;
        while (st1.hasMoreTokens()) attributes[x++] = st1.nextToken();

        System.out.println("Enter the Functional Dependencies:");
        
        String tmp2 = sc.nextLine();
        int num_fd = 1;
        for (int i = 0; i < tmp2.length(); i++) if (tmp2.charAt(i) == ';') num_fd++;
 		
        if (tmp2.length() == 0) 
        {
        	num_fd = 0;
        }

        String[] left = new String[num_fd];
        String[] right = new String[num_fd];

        if (num_fd != 0)
        {	
	        StringTokenizer st2 = new StringTokenizer(tmp2, ";"); 
	        x = 0;
	        while (st2.hasMoreTokens()) 
	        {
	        	String st3 = st2.nextToken();
	        	StringTokenizer st4 = new StringTokenizer(st3, ">");
	        	left[x] = st4.nextToken();
	        	right[x++] = st4.nextToken();
	        }
	    }
        String tmp3 = "";
		for (int i = 0; i < no_of_attributes; i++)
		{
			tmp3 += attributes[i] + ",";
			printCombination(attributes, no_of_attributes, i + 1, left, right);
		}
		

		CandidateKey.super_key.add(tmp3);

		Set<String> candidate_key = new HashSet<String>();
		
		for(String key : CandidateKey.super_key)
		{
			int ps = 0;
			int ck = 1;
			for (int i = 0; i < key.length(); i++)
			{
				if (key.charAt(i) == ',') ps -= -1;
			}
			String[] set = new String[ps];
			StringTokenizer st3 = new StringTokenizer(key, ",");
			x = 0;
			while (st3.hasMoreTokens()) set[x++] = st3.nextToken();
 
	        for (int i = 1; i < (1 << ps) - 1; i++) 
	        { 
	            String tmp4 = "";
	            for (int j = 0; j < ps; j++)
	                if ((i & (1 << j)) > 0) 
	                	tmp4 += set[j] + ",";

	            for (String key2: CandidateKey.super_key)
	            {
	            	if (tmp4.equals(key2))
	            	{
	            		ck = 0;
	            		break;
	            	}
	            }
	            if (ck == 0) break;
	        }
	        if (ck == 1) candidate_key.add(key);
		}

		System.out.println("Super Key(s): ");
		for(String key : CandidateKey.super_key)
		{
			String ans = "";
			for (int i = 0; i < key.length() - 1; i++) ans += key.charAt(i);
			System.out.println(ans);
		}
		

		System.out.println("\nCandidate Key(s)");
		for(String key : candidate_key)
		{
			String ans = "";
			for (int i = 0; i < key.length() - 1; i++) ans += key.charAt(i);
			System.out.println(ans);
		}
		

		System.out.println("\n\nThe normal forms satisfied are:\n");


		NormalForms n = new NormalForms();
		n.check_func(attributes, left, right, candidate_key);

		System.out.println("BCNF = " + n.bcnf);
		System.out.println("3-NF = " + n.nf3);
		System.out.println("2-NF = " + n.nf2);


		// If the relation is not in 2NF then converting to 2NF
		if (n.nf2 == 0)
		{
			System.out.println();
			Decompose d = new Decompose();
			d.decompose_to_2NF(attributes, left, right, candidate_key);
			String[] fds = new String[d.c];
			String[] rel = new String[d.c];
			String[] ckey = new String[d.c];
			System.out.println("Decomposition to 2 NF: ");


			for(String key : d.relation)
			{
				String temp1 = "";
				String temp2 = "";
				int check1 = 0;
				for (int i = 0; i < key.length(); i++)
				{
					if (key.charAt(i) == ':')
					{
						check1 = 1;
						continue;
					}
					if (check1 == 0) temp1 += key.charAt(i);
					if (check1 == 1) temp2 += key.charAt(i);
				}
				rel[Integer.parseInt(temp1)] = temp2;
			}

			for(String key : d.functional_dependency)
			{
				String temp1 = "";
				String temp2 = "";
				int check1 = 0;
				for (int i = 0; i < key.length(); i++)
				{
					if (key.charAt(i) == ':')
					{
						check1 = 1;
						continue;
					}
					if (check1 == 0) temp1 += key.charAt(i);
					if (check1 == 1) temp2 += key.charAt(i);
				}
				fds[Integer.parseInt(temp1)] = temp2;
			}

			for (int i = 0; i < d.c; i++)
			{
				int no_of_att = 0;
				for (int j = 0; j < rel[i].length(); j++)
				{
					if (rel[i].charAt(j) == ',') no_of_att++;
				}
				String[] att = new String[no_of_att];
				x = 0;
				StringTokenizer ps70 = new StringTokenizer(rel[i], ",");
				while (ps70.hasMoreTokens()) att[x++] = ps70.nextToken();
				
				Set<String> candidate = new HashSet<String>();
				int cou = 0;
				for (int z = 0; z < fds[i].length(); z++)
				{
					if (fds[i].charAt(z) == ';') cou++;
				}
				String[] leftt = new String[cou];
				String[] rightt = new String[cou];


				x = 0;
				StringTokenizer ps50 = new StringTokenizer(fds[i], ";");
				while (ps50.hasMoreTokens())
				{
					StringTokenizer ps99 = new StringTokenizer(ps50.nextToken(), ">");
					leftt[x] = ps99.nextToken();
					rightt[x++] = ps99.nextToken();
				}
				CandidateKey.super_key.clear();
				tmp3 = "";
				for (int j = 0; j < no_of_att; j++)
				{
					tmp3 += att[j] + ",";
					printCombination(att, no_of_att, j + 1, leftt, rightt);
				}
				

				CandidateKey.super_key.add(tmp3);

				
				
				for(String key : CandidateKey.super_key)
				{
					int ps = 0;
					int ck = 1;
					for (int g = 0; g < key.length(); g++)
					{
						if (key.charAt(g) == ',') ps -= -1;
					}
					String[] set = new String[ps];
					StringTokenizer st3 = new StringTokenizer(key, ",");
					x = 0;
					while (st3.hasMoreTokens()) set[x++] = st3.nextToken();

			        for (int g = 1; g < (1 << ps) - 1; g++) 
			        { 
			            String tmp4 = "";
			            for (int j = 0; j < ps; j++)
			                if ((g & (1 << j)) > 0) 
			                	tmp4 += set[j] + ",";

			            for (String key2: CandidateKey.super_key)
			            {
			            	if (tmp4.equals(key2))
			            	{
			            		ck = 0;
			            		break;
			            	}
			            }
			            if (ck == 0) break;
			        }
			        if (ck == 1) candidate.add(key);
				}
				String prakhar1 = "";
				for (String prakhar: candidate)
				{
					prakhar1 += prakhar + ";";
				}
				ckey[i] = prakhar1;
			}

			for (int i = 0; i < d.c; i++)
			{
				String ans = "";
				for (int j = 0; j < rel[i].length() - 1; j++) ans += rel[i].charAt(j);
				System.out.println("Relation " + (i + 1));
				System.out.print("Attributes: ");
				StringTokenizer ps95 = new StringTokenizer(ans, ",");
				while (ps95.hasMoreTokens())
				{
					System.out.print(ps95.nextToken() + " ");
				}
				System.out.println("\nFunctional Dependencies:");
				if (fds[i] == "") System.out.println("None");
				else
				{
					StringTokenizer ps100 = new StringTokenizer(fds[i], ";");
					while (ps100.hasMoreTokens())
					{
						StringTokenizer ps99 = new StringTokenizer(ps100.nextToken(), ">");
						System.out.println(ps99.nextToken() + "->" + ps99.nextToken());
					}
				}
				System.out.println("Candidate Key(s):");
				StringTokenizer ps80 = new StringTokenizer(ckey[i], ";");
				while (ps80.hasMoreTokens())
				{
					String ew = "";
					String we = ps80.nextToken();
					for (int j = 0; j < we.length() - 1; j++) ew += we.charAt(j);
					System.out.println(ew);
				}
				System.out.println();
			}
		}
		

		// If the relation is not in 3NF or BCNF then converting to BCNF

		String l1 = "";
		int dbms = 0;

		// Adding other functional dependencies required for result
		for (int k = 0; k < left.length; k++)
		{
			String y = right[k];
			String tempo = "";
			StringTokenizer ps98 = new StringTokenizer(right[k], ",");
			while (ps98.hasMoreTokens())
			{
				int ch = 1;
				String abcd = ps98.nextToken();
				StringTokenizer ps97 = new StringTokenizer(left[k], ",");
				while (ps97.hasMoreTokens())
				{
					if (abcd.equals(ps97.nextToken()))
					{
						ch = 0;
						break;
					}
				}
				if (ch == 1)
				{
					tempo += abcd + ",";
				}
			}
			String tempo2 = "";
			for (int i = 0; i < tempo.length() - 1; i++) tempo2 += tempo.charAt(i);
			if (tempo2.length() > 0)
			{
				l1 += left[k] + ">" + tempo2 + ";";
				dbms++;
			}
		}
		
		for (int k = 0; k < left.length; k++)
		{
			StringTokenizer ps60 = new StringTokenizer(right[k], ",");
			while (ps60.hasMoreTokens())
			{
				String sp10 = ps60.nextToken();
				for (int j = 0; j < left.length; j++)
				{
					int ch = 0;
					if (j == k) continue;
					StringTokenizer ps61 = new StringTokenizer(left[j], ",");
					while (ps61.hasMoreTokens())
					{
						if (sp10.equals(ps61.nextToken()))
						{
							ch = 1;
							break;
						}
					}
					if (ch == 1)
					{
						Set<String> temporary = new HashSet<String>();
						StringTokenizer ps62 = new StringTokenizer(left[k], ",");
						while (ps62.hasMoreTokens()) temporary.add(ps62.nextToken());
						StringTokenizer ps63 = new StringTokenizer(left[j], ",");
						while (ps63.hasMoreTokens())
						{
							int ch2 = 1;
							String sp11 = ps63.nextToken();
							StringTokenizer ps64 = new StringTokenizer(left[k] + "," + right[k], ",");
							while(ps64.hasMoreTokens())
							{
								if (sp11.equals(ps64.nextToken()))
								{
									ch2 = 0;
									break;
								}
							}
							if (ch2 == 1) temporary.add(sp11);
						}
						StringTokenizer ps65 = new StringTokenizer(right[j], ",");
						while (ps65.hasMoreTokens())
						{
							String sp12 = ps65.nextToken();
							int ch3 = 1;
							for (String key: temporary)
							{
								if (key.equals(sp12))
								{
									ch3 = 0;
									break;
								}
							}
							StringTokenizer ps66 = new StringTokenizer(right[k], ",");
							while (ps66.hasMoreTokens())
							{
								if (sp12.equals(ps66.nextToken()))
								{
									ch3 = 0;
									break;
								}
							}
							if (ch3 == 1)
							{
								String sp13 = "";
								for (String key: temporary) sp13 += key + ",";
								String sp14 = "";
								for (int z = 0; z < sp13.length() - 1; z++) sp14 += sp13.charAt(z);
								l1 += sp14 + ">" + sp12 + ";";
								dbms++;
							}
						}
					}
				}
			}
		}

		String[] leftt = new String[dbms];
        String[] rightt = new String[dbms];

        StringTokenizer stt2 = new StringTokenizer(l1, ";"); 
        x = 0;
        while (stt2.hasMoreTokens()) 
        {
        	String st3 = stt2.nextToken();
        	StringTokenizer st4 = new StringTokenizer(st3, ">");
        	leftt[x] = st4.nextToken();
        	rightt[x++] = st4.nextToken();
        }
        if (n.bcnf == 0)
		{
			System.out.println();
			Decompose d = new Decompose();
			d.decompose_to_BCNF(attributes, leftt, rightt, candidate_key);
			String[] fds = new String[d.c];
			String[] rel = new String[d.c];
			String[] ck = new String[d.c];
			System.out.println("Decomposition to BCNF: ");


			for(String key : d.relation)
			{
				String temp1 = "";
				String temp2 = "";
				int check1 = 0;
				for (int i = 0; i < key.length(); i++)
				{
					if (key.charAt(i) == ':')
					{
						check1 = 1;
						continue;
					}
					if (check1 == 0) temp1 += key.charAt(i);
					if (check1 == 1) temp2 += key.charAt(i);
				}
				rel[Integer.parseInt(temp1)] = temp2;
			}

			for(String key : d.functional_dependency)
			{
				String temp1 = "";
				String temp2 = "";
				int check1 = 0;
				for (int i = 0; i < key.length(); i++)
				{
					if (key.charAt(i) == ':')
					{
						check1 = 1;
						continue;
					}
					if (check1 == 0) temp1 += key.charAt(i);
					if (check1 == 1) temp2 += key.charAt(i);
				}
				fds[Integer.parseInt(temp1)] = temp2;
			}

			for(String key : d.candidate)
			{
				String temp1 = "";
				String temp2 = "";
				int check1 = 0;
				for (int i = 0; i < key.length(); i++)
				{
					if (key.charAt(i) == ':')
					{
						check1 = 1;
						continue;
					}
					if (check1 == 0) temp1 += key.charAt(i);
					if (check1 == 1) temp2 += key.charAt(i);
				}
				ck[Integer.parseInt(temp1)] = temp2;
			}

			for (int i = 0; i < d.c; i++)
			{
				String ans = "";
				for (int j = 0; j < rel[i].length() - 1; j++) ans += rel[i].charAt(j);
				System.out.println("Relation " + (i + 1));
				System.out.print("Attributes: ");
				StringTokenizer ps95 = new StringTokenizer(ans, ",");
				while (ps95.hasMoreTokens())
				{
					System.out.print(ps95.nextToken() + " ");
				}
				System.out.println("\nFunctional Dependencies:");
				if (fds[i] == "") System.out.println("None");
				else
				{
					StringTokenizer ps100 = new StringTokenizer(fds[i], ";");
					while (ps100.hasMoreTokens())
					{
						StringTokenizer ps99 = new StringTokenizer(ps100.nextToken(), ">");
						System.out.println(ps99.nextToken() + "->" + ps99.nextToken());
					}
				}
				System.out.println("Candidate Key(s):");
				StringTokenizer ps80 = new StringTokenizer(ck[i], ".");
				while (ps80.hasMoreTokens())
				{
					String ew = "";
					String we = ps80.nextToken();
					for (int j = 0; j < we.length() - 1; j++) ew += we.charAt(j);
					System.out.println(ew);
				}
				System.out.println();
			}
		}
	}
}


// TEST CASES:

// 1.
// A B C D E F G H I J
// A,B>C;A,D>G,H;B,D>E,F;A>I;H>J

// 2.
// A B C D E
// A>B;B>E;C>D

// 3.
// A B C D E
// A,B>C;D>E

// 4.
// A B C D
// A>B,C,D;B,C>A,D;D>B

// 5.
// A B C D E F
// A>B,C,D,E,F;B,C>A,D,E,F;D,E,F>A,B,C

// 6.
// A B C D E
// A>B;B,C>E;D,E>A

// 7.
// A B C D E
// A,B>C,D;D>A;B,C>D,E

// 8.
// W X Y Z
// Z>W;Y>X,Z;X,W>Y

// 9.
// A B C D E F
// A,B>C;D,C>A,E;E>F

// 10.
// V W X Y Z
// Z>Y;Y>Z;X>Y,V;V,W>X

// 11.
// A B C D E F
// A,B,C>D;A,B,D>E;C,D>F;C,D,F>B;B,F>D

// 12.
// A B C D E F G H
// A,B>C;A>D,E;B>F;F>G,H

// 13.
// A B C D E
// C,E>D;D>B;C>A

// 14.
// A B C D E F
// A,B>C;D,C>A,E;E>F

// 15.
// A B C D E F G H I
// A,B>C;B,D>E,F;A,D>G,H;A>I

// 16.
// A B C D E
// A,B>C,D;D>A;B,C>D,E

// 17.
// A B C D E
// B,C>A,D,E;D>B

// 18.
// A B C D E F G H I J
// A,B>C;A>D,E;B>F;F>G,H;D>I,J

// 19.
// A B C D E
// A,B>C;B>D;D>E

// 20.
// A B C
// A,B>C;C>A

// 21.
// A B C D E F
// A,B,C>D,E,F;A,B>E,F;A>B

// 22.
// A B C D E F
// A,B,C>D,E,F;A,B>E,F;A>E

// 23.
// A B C D E F
// A,B,C>D,E,F;A,B>E,F;A>E,F
