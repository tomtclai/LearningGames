using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SpaceSmasherLib.Entities
{
    public abstract class Set
    {
        public Set()
        {
            initialize();
        }

        public abstract void initialize();

        public abstract void add();

        public virtual void add(int num)
        {
            for (int i = 0; i < num; i++)
                add();
        }
    }
}
