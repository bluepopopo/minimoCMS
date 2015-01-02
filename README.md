<h1 id="minimocms-a-developer-first-cms">minimoCMS - A developer-first CMS</h1>

<p>minimoCMS website: <a href="http://www.minimocms.com">http://www.minimocms.com</a></p>

<p><div class="toc"><div class="toc">
<ul>
<li><a href="#minimocms-a-developer-first-cms">minimoCMS - A developer-first CMS</a><ul>
<li><a href="#introduction">Introduction</a></li>
<li><a href="#getting-started">Getting Started</a><ul>
<li><a href="#prerequisites">Prerequisites</a></li>
<li><a href="#initialise">Initialise</a></li>
<li><a href="#define-data">Define Data</a></li>
<li><a href="#data-types">Data Types</a></li>
<li><a href="#items">Items</a></li>
</ul>
</li>
<li><a href="#examples">Examples</a><ul>
<li><a href="#example-data-definition">Example Data Definition</a></li>
<li><a href="#example-data-placement-in-site">Example Data Placement in Site</a></li>
</ul>
</li>
<li><a href="#deployment-options">Deployment Options</a></li>
</ul>
</li>
</ul>
</div>
</div>
</p>

<h2 id="introduction">Introduction</h2>

<p>minimoCMS allows you to define the structure of the <em>data</em> in your website <br>
using a simple API. The API allows you to define MoPage’s, and a MoPage can <br>
contain combinations of 3 main types - MoDocument, MoList and MoItem. From <br>
these types you can create detailed page structures. These pages do not <br>
write or generate any html/css for your website, the look/feel/structure of <br>
the website is left to the developer, minimoCMS only works with the <em>data</em> <br>
in your site.</p>

<p>Once you’ve defined the data structure using the minimoCMS APIs you now <br>
need to place the data in your site. minimoCMS does not do this directly <br>
for you, you must first get the required page, and then insert the data <br>
where it is required in your site. You may think that this is a down-side <br>
to minimo as it requires more work for the developer, however it is <br>
actually one of the unique benefits of minimo as it remains non-invasive, <br>
your site isn’t <em>controlled</em> by minimoCMS and in fact if ever you wanted to <br>
move off minimo it would not be a complex task.</p>

<p>Although minimoCMS is flexible to use any web language/framework and any <br>
storage engine or DB, for the purpose of simplicity this guide will cover <br>
the defaults - Spark Java web framework <a href="http://www.sparkjava.com">http://www.sparkjava.com</a> and <br>
MongoDB <a href="http://www.mondodb.com">http://www.mondodb.com</a> .</p>



<h2 id="getting-started">Getting Started</h2>

<h3 id="prerequisites">Prerequisites</h3>

<p>First things first - create a web-app project and add the minimoCMS maven <br>
dependency</p>

<pre class="prettyprint"><code class="language-xml hljs "><span class="hljs-tag">&lt;<span class="hljs-title">dependency</span>&gt;</span>
   <span class="hljs-tag">&lt;<span class="hljs-title">groupId</span>&gt;</span>com.minimocms<span class="hljs-tag">&lt;/<span class="hljs-title">groupId</span>&gt;</span>
   <span class="hljs-tag">&lt;<span class="hljs-title">artifactId</span>&gt;</span>minimo<span class="hljs-tag">&lt;/<span class="hljs-title">artifactId</span>&gt;</span>
   <span class="hljs-tag">&lt;<span class="hljs-title">version</span>&gt;</span>1.0<span class="hljs-tag">&lt;/<span class="hljs-title">version</span>&gt;</span>
<span class="hljs-tag">&lt;/<span class="hljs-title">dependency</span>&gt;</span></code></pre>

<p>You will need Java 8 to run minimoCMS. <br>
You will also need MongoDB running on localhost if you are using the default data store MongoDataStoreImpl (i.e. you are not planning to implement DataStoreInterface).</p>

<h3 id="initialise">Initialise</h3>

<p>Because we are running an All-in-One web-app (see Deployment Options) we <br>
will initialize minimoCMS like so -</p>

<pre class="prettyprint"><code class="language-java hljs "> <span class="hljs-keyword">public</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">void</span> <span class="hljs-title">main</span>(String args[]){
        Minimo.init(<span class="hljs-string">"minimoCMSSample"</span>, <span class="hljs-keyword">new</span>
MongoDataStoreImpl(<span class="hljs-string">"minimoCMSSample"</span>));
        ...
}</code></pre>

<p>This has done 2 things - <br>
1. Initialised minimoCMS with the siteName specified in the first argument <br>
2. Initialised the MongoDB data store, the store will use the same site <br>
name “minimoCMSSample” <br>
    Here is where you would specify your own implementation of <br>
DataStoreInterface if you don’t want to use MongoDB. Note in this example <br>
you will need MongoDB running on your localhost for this data store to work.</p>



<h3 id="define-data">Define Data</h3>

<p>The Sample web-app creates data in a separate function, here is the full <br>
main method</p>

<pre class="prettyprint"><code class="language-java hljs "><span class="hljs-keyword">public</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">void</span> <span class="hljs-title">main</span>(String args[]){
        Minimo.init(<span class="hljs-string">"minimoCMSSample"</span>, <span class="hljs-keyword">new</span>
MongoDataStoreImpl(<span class="hljs-string">"minimoCMSSample"</span>));
        <span class="hljs-keyword">if</span>(Minimo.pages().size()==<span class="hljs-number">0</span>)createData();
        Minimo.persistPages();
        createWeb(); <span class="hljs-comment">// this will be covered later</span>
}</code></pre>

<p>Ignore the last line, that is where your web-app will be defined. The line <br>
after init() checks to see if pages have already been defined, if not we <br>
create the data. The second-last line persists or saves the pages which <br>
were created.</p>



<h3 id="data-types">Data Types</h3>

<ul>
<li><strong>Page</strong>: MoPage.class <br>
A Page is as the name suggests, intended to be used per page in your <br>
website. It can contain any number of Documents, Lists and Items</li>
<li><strong>Document</strong>: MoDoc.class <br>
A Document is a structure intended for individual sections of a page. <br>
Similar to a page it can contain a number of other Documents, Lists and <br>
Items</li>
<li><strong>List</strong>: MoList.class <br>
A list is used for repititious content of consistent structure. E.g. a <br>
blog, or a list of products. Because each list item must have the same <br>
structure, you must define this structure when you create a list. The <br>
structure is built using a MoDoc, which as previously mentioned can contain <br>
Documents, Lists and Items. Therefore the only restriction on the list is <br>
that each item must have the same structure (but not necessarily the same <br>
data, this includes number of items in sub-lists).</li>
<li><strong>Item</strong>: MoItem.class <br>
An item is a piece of data which is actually rendered in a site. <br>
MoItem.class is not used directly, but it is the supertype for a number of <br>
other types of similar definition e.g. MoTextItem and MoImage. Items cannot <br>
contain documents, lists or other items.</li>
</ul>

<h3 id="items">Items</h3>



<h2 id="examples">Examples</h2>

<h3 id="example-data-definition">Example Data Definition</h3>

<p>I’ve included a simple example -</p>

<pre class="prettyprint"><code class="language-java hljs ">page(<span class="hljs-string">"Home"</span>).document(<span class="hljs-string">"Main Content"</span>).list(<span class="hljs-string">"Products"</span>).buildTemplate(<span class="hljs-keyword">new</span>
MoDoc(<span class="hljs-string">"Product"</span>),doc-&gt;{
doc.addItem(<span class="hljs-keyword">new</span> MoTextItem(<span class="hljs-string">"Title"</span>),
txt -&gt; txt.setValue(<span class="hljs-string">"My Product Title"</span>));

doc.addItem(<span class="hljs-keyword">new</span> MoTextAreaItem(<span class="hljs-string">"Description"</span>),
txt -&gt; txt.setValue(<span class="hljs-string">"A product by the people, for the people"</span>));

doc.addItem(<span class="hljs-keyword">new</span> MoImageItem(<span class="hljs-string">"Image"</span>),
f -&gt; f.file(<span class="hljs-keyword">new</span> ResourceUtil().getFileBytes(<span class="hljs-string">"/assets/images/800x500.gif"</span>)));

doc.addItem(<span class="hljs-keyword">new</span> MoTextItem(<span class="hljs-string">"Buy Button"</span>),
txt -&gt; txt.setValue(<span class="hljs-string">"Buy Now"</span>));

doc.addItem(<span class="hljs-keyword">new</span> MoTextItem(<span class="hljs-string">"Info-Button"</span>),
txt -&gt; txt.setValue(<span class="hljs-string">"More Info"</span>));
});

page(<span class="hljs-string">"Home"</span>).document(<span class="hljs-string">"Body"</span>).list(<span class="hljs-string">"Products"</span>).add(<span class="hljs-number">4</span>);</code></pre>

<p>This is quite a detailed definition, so let’s break it down starting from <br>
the top. <br>
First all the methods we are using are statically defined in Minimo.class, <br>
so we need to start with</p>



<pre class="prettyprint"><code class="language-java hljs "><span class="hljs-keyword">import</span> <span class="hljs-keyword">static</span> com.minimocms.Minimo;</code></pre>

<p>Next we need to create a page. The following creates the page of name <br>
“Home”, or if the “Home” page were previously defined it would get this <br>
page instance.</p>



<pre class="prettyprint"><code class="language-java hljs ">page(<span class="hljs-string">"Home"</span>)</code></pre>

<p>With the page we create a document.</p>



<pre class="prettyprint"><code class="language-java hljs ">page(<span class="hljs-string">"Home"</span>).document(<span class="hljs-string">"Main Content"</span>)</code></pre>

<p>And in the document we create a list of products (we name this list <br>
“Products”).</p>



<pre class="prettyprint"><code class="language-java hljs ">page(<span class="hljs-string">"Home"</span>).document(<span class="hljs-string">"Main Content"</span>).list(<span class="hljs-string">"Products"</span>)</code></pre>

<p>And as mentioned in the previous section, we need to define the structure <br>
of the items in our list. We are going to define the items as MoDoc’s, and <br>
then we are going to ‘build’ (or define) the MoDoc.</p>



<pre class="prettyprint"><code class="language-java hljs ">page(<span class="hljs-string">"Home"</span>).document(<span class="hljs-string">"Main Content"</span>).list(<span class="hljs-string">"Products"</span>).buildTemplate(<span class="hljs-keyword">new</span>
MoDoc(<span class="hljs-string">"Product"</span>), doc -&gt; {
   <span class="hljs-comment">//Our "Product" document is defined here</span>
});</code></pre>



<h3 id="example-data-placement-in-site">Example Data Placement in Site</h3>

<p>Coming back to the main() method before, right after we defined and persisted the data definition we ignored the line with createWeb(). Simply it creates a web-route, inserts the data we created into our model and renders a velocity template with html/css (and our data mixed in). Here’s the createWeb() method -</p>

<pre class="prettyprint"><code class="language-java hljs "><span class="hljs-keyword">private</span> <span class="hljs-keyword">static</span> <span class="hljs-keyword">void</span> <span class="hljs-title">createWeb</span>() {
   get(<span class="hljs-string">"/"</span>,(req,resp)-&gt;{
       Map&lt;String,Object&gt; model = <span class="hljs-keyword">new</span> HashMap&lt;&gt;();
       model.put(<span class="hljs-string">"page"</span>,page(<span class="hljs-string">"Home"</span>)); <span class="hljs-comment">//the home page and all data in it</span>
       <span class="hljs-keyword">return</span> <span class="hljs-keyword">new</span> ModelAndView(model,<span class="hljs-string">"/index.vm"</span>); <span class="hljs-comment">//the velocity template we're rendering</span>
   }, <span class="hljs-keyword">new</span> VelocityTemplateEngine());
}</code></pre>

<p>So using Spark we’ve defined a default route “/”, put our “Home” page definition in the model, and rendered our view with the “index.vm” velocity template. Of course you could potentially use any template engine here.</p>

<p>The next step is for the “index.vm” velocity template to render the html/css/js for the website with our data mixed in. I will skip the generic html/css code as we are most concerned about how the data is rendered. Let’s have a look at an exerpt:</p>



<pre class="prettyprint"><code class="language-java hljs ">&lt;!-- Page Features --&gt;
#foreach($doc in $page.document(<span class="hljs-string">"Main Content"</span>).list(<span class="hljs-string">"Products"</span>).items())
    &lt;div class=<span class="hljs-string">"col-md-3 col-sm-6 hero-feature"</span>&gt;
        &lt;div class=<span class="hljs-string">"thumbnail"</span>&gt;
            &lt;img src=<span class="hljs-string">"$doc.get("</span>Image<span class="hljs-string">").text()"</span> alt=<span class="hljs-string">""</span>&gt;
            &lt;div class=<span class="hljs-string">"caption"</span>&gt;
                &lt;h3&gt;$doc.get(<span class="hljs-string">"Title"</span>).text()&lt;/h3&gt;
                &lt;p&gt;$doc.get(<span class="hljs-string">"Description"</span>).text()&lt;/p&gt;
                &lt;p&gt;
                    &lt;a href=<span class="hljs-string">"#"</span> class=<span class="hljs-string">"btn btn-primary"</span>&gt;$doc.item(<span class="hljs-string">"Buy Button"</span>).text()&lt;/a&gt;
                    &lt;a href=<span class="hljs-string">"#"</span> class=<span class="hljs-string">"btn btn-default"</span>&gt;$doc.item(<span class="hljs-string">"Info Button"</span>).text()&lt;/a&gt;
                &lt;/p&gt;
            &lt;/div&gt;
        &lt;/div&gt;
    &lt;/div&gt;
#end</code></pre>

<p>$page refers directly to our “Home” page we inserted into the model. From there you can see we are accessing the data in almost the exact same way as we defined it. We are iterating over the MoDoc’s in our Products list, and rendering an image, title, description and 2 button. One thing we would include in a real website is a link for the Buy and Info buttons, something like this:</p>



<pre class="prettyprint"><code class="language-java hljs ">&lt;a href=<span class="hljs-string">"$doc.item("</span>Buy URL<span class="hljs-string">").text()"</span> class=<span class="hljs-string">"btn btn-primary"</span>&gt;$doc.item(<span class="hljs-string">"Buy Button"</span>).text()&lt;/a&gt;
&lt;a href=<span class="hljs-string">"$doc.item("</span>Info URL<span class="hljs-string">").text()"</span> class=<span class="hljs-string">"btn btn-default"</span>&gt;$doc.item(<span class="hljs-string">"Info Button"</span>).text()&lt;/a&gt;</code></pre>



<h2 id="deployment-options">Deployment Options</h2>

<p>minimoCMS can be run with your website in two ways</p>

<ol>
<li><p>All-in-one web-app <br>
Run the minimoCMS management console in the same web-app as your site. <br>
The only restriction with this method is that you are required to use Spark <br>
as your Java web framework</p></li>
<li><p>Separate CMS and web-app <br>
This method keeps the web-interface to minimoCMS separate to your <br>
web-app. Your web-app will still need to use minimoCMS to define and get <br>
data (or alternatively use the REST API). With the REST API you can use <br>
minimoCMS with any language or web framework (e.g. PHP).</p></li>
</ol>
