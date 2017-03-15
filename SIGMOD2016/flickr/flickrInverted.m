set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x = [1, 2, 3, 4, 5];
y1 = [179.7846424	159.9772691	155.0203639	141.8510629	134.4115903];
y2 = [141.8416215	130.9211995	128.5573833	122.2162846	120.8604103];
y3 = [1996.11	1357.11	1094.3	908.68	811.58];
y4 = [980.68	862.67	815.12	753.35	706.02];


p1= semilogy(x, y1, '-ks');
hold on;
p2 = semilogy(x, y2, '-kd');
hold on;
p3 = semilogy(x, y3, '-kv');
hold on;
p4 = semilogy(x, y4, '-k^');
hold on;

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 10000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
%leg=legend('basic-g','basic-w','Inc-S','Inc-T','Dec', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'Inc-S','Inc-T');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4],'Inc-S*','Inc-T*');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


