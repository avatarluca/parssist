package parssist.parser.bottom_up_analysis.lrparser.lr0parser.parser.util.tree.visitor;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import parssist.parser.bottom_up_analysis.lrparser.lr0parser.parser.util.tree.ParseTreeNode;


/**
 * JSON like Visitor implementation.
 * Because of webassembly, the visitor can't use the org.json library. 
 * So the visitor uses a custom implementation of a JSON like object.
 * @param <T> Type of the object to visit.
 */
public class JsonLikeTreeVisitor implements Visitor {
    private final Map<String, Object> data;


    /**
     * Creates a new JsonLikeTree visitor.
     */
    public JsonLikeTreeVisitor() {
        this.data = new HashMap<>();
    }


    public String getJson() {
        return JsonObject.mapToJsonString(data);
    }


    @Override public void visit(ParseTreeNode node) {
        List<Object> kids = new ArrayList();

        if(node.getChildren().size() > 0) {
            for(ParseTreeNode child : node.getChildren()) {
                final JsonLikeTreeVisitor visitor = new JsonLikeTreeVisitor();
                child.accept(visitor);
                kids.add(visitor.getJson());
            }
        }

        this.data.put(node.getToken().symbol(), kids);
    }
}