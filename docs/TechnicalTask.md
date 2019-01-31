## Technical task
* Main functionality:
1. User registration.
2. Authorization.
3. Adding an article.
4. Editing an article.
5. Deleting an article.
6. Editing user.

* Additional functionality:
1. Adding / removing a article like.
2. Adding / removing comments on the article.
3. Add user to subscriptions.
4. Search.

* Details:
1. During registration, the login is checked (it must be unique).

2. An article may have one author, several tags (article categories).
In article can be: lines of code, embedded video (When added, you can click on youtube or another),
text, quotes, links, headings, paragraphs, pictures. The title of the article does not have to be unique.
For storing likes and comments, make the database denormalize (storing the number of likes and comments in each article)
in order to increase performance (when requesting multiple articles, the system would be subject to high load since each
time it would be necessary to read the number from the table).

3. Only the author of the article can edit.

4. Only an author can delete an article - after it all its resources are deleted (comments, likes, viewing statistics,
all pictures, links, etc.).
* Additionally details:
1. The user can view a list of their likes and comments.
2. The comment may respond to other comments.
