# Wiki-MID
http://wikimid.tweets.di.uniroma1.it/wikimid/<br>
https://doi.org/10.6084/m9.figshare.6231326.v1<br>
Giorgia Di Tommaso(1), Stefano Faralli(2), Giovanni Stilo(1), Paola Velardi(1)<br>
(1) University of Rome Sapienza<br>
(2) University of Rome Unitelma Sapienza<br>

This is the code repository to share part of the pipiline components used for the construction of the Wiki-MID resource.<br>
We are improving both the code design and documentation. 

# Project Description

Wiki-MID is a LOD compliant multi-domain interests dataset to train and test Recommender Systems. Our English dataset includes an average of 90 multi-domain preferences per user on music, books, movies, celebrities, sport, politics and much more, for about half million Twitter users traced during six months in 2017. Preferences are either extracted from messages of users who use Spotify, Goodreads and other similar content sharing platforms, or induced from their "topical" friends, i.e., followees representing an interest rather than a social relation between peers. In addition, preferred items are matched with Wikipedia articles describing them. This unique feature of our dataset provides a mean to categorize preferred items, exploiting available semantic resources linked to Wikipedia such as the Wikipedia Category Graph, DBpedia, BabelNet and others.
Our resource is designed on top of the Semantically-Interlinked Online Communities (SIOC) core ontology. The SIOC ontology favors the inclusion of data mined from social networks communities into the Linked Open Data (LOD) cloud. As shown in Figure 1 we represent Twitter users as instances of the SIOC UserAccount class, and tweets as instances of the SIOC Post class. Interests (i.e., topical user interests and message based user interests) are then associated to a user trough the SIOC predicate about. Finally, trough the usage of the Simple Knowledge Organization System Namespace Document (SKOS) predicate relatedMatch, for each associated interest we provide a corresponding Wikipedia page as a result of our automated mapping methodology.

# License
https://creativecommons.org/licenses/by/4.0/

# Mapping Twitter Users to Wikipedia

There are currently two main classes, it.uniroma1.lcl.wikimid.mapping.Twitter2WikipediaEN and it.uniroma1.lcl.wikimid.mapping.Twitter2WikipediaIT, for English and Italian respectively. <br>
The two classes receive as command shell argument the dataset filename of twitter user profiles to be mapped into wikipedia pages, and output a file in the same path with extension ".mapping.tsv".<br>
The input file is a tabbaed separated file with information from retrived Twitter user-objects (https://developer.twitter.com/en/docs/tweets/data-dictionary/overview/user-object) the following format:<br>

Id TAB ScreenName TAB Name TAB Location TAB Description TAB URL TAB Verified TAB FollowersCount TAB FriendsCount TAB ListedCount TAB StatusesCount TAB CreatedAt TAB Lang TAB WithheldInCountries <br>
where:<br>
<ul>
 <li><b>Id:</b> corresponds to the field <i>id</i> of a user-object</li>
  <li><b>ScreenName:</b> corresponds to the field <i>screen_name</i> of a user-object</li>
  <li><b>Name:</b> corresponds to the field <i>name</i> of a user-object</li>
  <li><b>Location:</b> corresponds to the field <i>location</i> of a user-object</li>
  <li><b>Description:</b> corresponds to the field <i>description</i> of a user-object</li>
  <li><b>URL:</b> corresponds to the field <i>url</i> of a user-object</li>
   <li><b>Verified:</b> corresponds to the field <i>verified</i> of a user-object</li>
  <li><b>FollowersCount:</b> corresponds to the field <i>followers_count</i> of a user-object</li>
    <li><b>FriendsCount:</b> corresponds to the field <i>friends_count</i> of a user-object</li>
  <li><b>ListedCount:</b> corresponds to the field <i>listed_count</i> of a user-object</li>
    <li><b>StatusesCount:</b> corresponds to the field <i>statuses_count</i> of a user-object</li>
  <li><b>CreatedAt:</b> corresponds to the field <i>created_at</i> of a user-object</li>
     <li><b>Lang:</b> corresponds to the field <i>lang</i> of a user-object</li>
  <li><b>WithheldInCountries:</b> corresponds to the field <i>withheld_in_countries</i> of a user-object</li>
 
 </ul>






