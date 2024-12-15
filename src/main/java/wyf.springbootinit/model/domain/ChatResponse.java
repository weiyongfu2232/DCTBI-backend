package springbootinit.model.domain;//这个是用来处理响应的
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    Output output;
    Usage usage;
    String request_id;

    public class Output{
        public String text;
        public String finish_reason;
    }
    public class Usage{
        public String output_tokens;
        public String input_tokens;
    }
}
