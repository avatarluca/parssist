package parssist.parser.top_down_analysis.nrdparser.util.tree.visitor;

import org.json.JSONArray;
import org.json.JSONObject;

import parssist.parser.top_down_analysis.nrdparser.util.tree.ParseTreeNode;


/**
 * JSON Visitor implentation.
 * @param <T> Type of the object to visit.
 */
public class JsonTreeVisitor implements Visitor {
    private final JSONObject json;


    /**
     * Creates a new JSON visitor.
     */
    public JsonTreeVisitor() {
        this.json = new JSONObject();
    }


    public JSONObject getJson() {
        return json;
    }


    @Override public void visit(ParseTreeNode node) {
        final JSONArray kids = new JSONArray();

        if (node.getChildren().size() > 0) {
            for (ParseTreeNode child : node.getChildren()) {
                final JsonTreeVisitor visitor = new JsonTreeVisitor();
                child.accept(visitor);
                kids.put(visitor.getJson());
            }
        } 

        this.json.put(node.getToken().symbol(), kids);
    }
}