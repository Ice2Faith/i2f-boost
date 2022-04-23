package i2f.springboot.shiro;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @author ltb
 * @date 2022/4/23 19:40
 * @desc
 */
@Data
@NoArgsConstructor
public class SessionControlWebSubjectFactory extends DefaultWebSubjectFactory {
    private boolean enableSession=false;
    public SessionControlWebSubjectFactory(boolean enableSession){
        this.enableSession=enableSession;
    }
    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(enableSession);
        return super.createSubject(context);
    }
}
