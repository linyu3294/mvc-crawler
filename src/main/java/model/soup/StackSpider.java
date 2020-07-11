package model.soup;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * A StackSpider class extends from AbstracSpider and is a sibling to SoupSpider. Some differences
 * are it uses a breadth-first-search strategy as opposed to recursive. It also has an additional
 * feature that allows recording of each url loading time that it visits. This feature was not
 * available in SoupSpider.
 */
public class StackSpider extends AbstractSpider {
   Stack <HashMap<String, String>> stack = new Stack<HashMap<String, String>>();

/**
 * Constructor for StackSoup.
 */
protected StackSpider (String resourcesFolder) {
   super(resourcesFolder);
}

/**
 * StackSpider can crawl a url and its children and verify their contents. StackSoup uses a stack
 * with a bsf strategy as opposed to recursive strategy implemented in SoupSpider.
 */
@Override
public void crawl (String url, String parent) throws IOException {

}
}


