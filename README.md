# Wiki-Mid
http://wikimid.tweets.di.uniroma1.it/wikimid/
https://figshare.com/articles/Wiki-MID_Dataset_LOD_TSV_/6231326/1
* Giorgia Di Tommaso, Stefano Faralli, Giovanni Stilo, Paola Velardi

# Project Description:
Wiki-MID is a LOD compliant multi-domain interests dataset to train and test Recommender Systems. Our English dataset includes an average of 90 multi-domain preferences per user on music, books, movies, celebrities, sport, politics and much more, for about half million Twitter users traced during six months in 2017. Preferences are either extracted from messages of users who use Spotify, Goodreads and other similar content sharing platforms, or induced from their "topical" friends, i.e., followees representing an interest rather than a social relation between peers. In addition, preferred items are matched with Wikipedia articles describing them. This unique feature of our dataset provides a mean to categorize preferred items, exploiting available semantic resources linked to Wikipedia such as the Wikipedia Category Graph, DBpedia, BabelNet and others.
Our resource is designed on top of the Semantically-Interlinked Online Communities (SIOC) core ontology. The SIOC ontology favors the inclusion of data mined from social networks communities into the Linked Open Data (LOD) cloud. As shown in Figure 1 we represent Twitter users as instances of the SIOC UserAccount class, and tweets as instances of the SIOC Post class. Interests (i.e., topical user interests and message based user interests) are then associated to a user trough the SIOC predicate about. Finally, trough the usage of the Simple Knowledge Organization System Namespace Document (SKOS) predicate relatedMatch, for each associated interest we provide a corresponding Wikipedia page as a result of our automated mapping methodology.

# License
https://creativecommons.org/licenses/by/4.0/

# Documentation
 This is the code repository to share part of the pipiline components used for the construction of the Wiki-MID resource
 There are several aspects of the project (source and documentation) we are improving. 


